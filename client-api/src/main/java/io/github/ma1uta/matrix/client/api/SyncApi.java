/*
 * Copyright Anatoliy Sablin tolya@sablin.xyz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.matrix.client.api;

import io.github.ma1uta.matrix.client.model.sync.SyncResponse;
import io.github.ma1uta.matrix.common.ErrorResponse;
import io.github.ma1uta.matrix.common.Page;
import io.github.ma1uta.matrix.common.Secured;
import io.github.ma1uta.matrix.event.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**
 * To read events, the intended flow of operation is for clients to first call the /sync API without a since parameter.
 * This returns the most recent message events for each room, as well as the state of the room at the start of the returned timeline.
 * The response also includes a next_batch field, which should be used as the value of the since parameter in the next call to /sync.
 * Finally, the response includes, for each room, a prev_batch field, which can be passed as a start parameter to
 * the /rooms/&lt;room_id&gt;/messages API to retrieve earlier messages.
 */
@Path("/_matrix/client/r0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SyncApi {

    /**
     * PresenceContent.
     */
    class Presence {

        protected Presence() {
        }

        /**
         * Offline.
         */
        public static final String OFFLINE = "offline";
    }

    /**
     * Synchronise the client's state with the latest state on the server. Clients use this API when they first log in to get
     * an initial snapshot of the state on the server, and then continue to call this API to get incremental deltas to the state,
     * and to receive new messages.
     * <br>
     * <b>Requires auth:</b> Yes.
     * <br>
     * Return: {@link SyncResponse}.
     * <p>Status code 200: The initial snapshot or delta for the client to use to update their state.</p>
     *
     * @param filter          The ID of a filter created using the filter API or a filter JSON object encoded as a string. The server will
     *                        detect whether it is an ID or a JSON object by whether the first character is a "{" open brace. Passing the
     *                        JSON inline is best suited to one off requests. Creating a filter using the filter API is recommended for
     *                        clients that reuse the same filter multiple times, for example in long poll requests.
     * @param since           A point in time to continue a sync from.
     * @param fullState       Controls whether to include the full state for all rooms the user is a member of.
     *                        <br>
     *                        If this is set to true, then all state events will be returned, even if since is non-empty.
     *                        The timeline will still be limited by the since parameter. In this case, the timeout parameter will be ignored
     *                        and the query will return immediately, possibly with an empty timeline.
     *                        <br>
     *                        If false, and since is non-empty, only state which has changed since the point indicated by since will be
     *                        returned.
     *                        <br>
     *                        By default, this is false.
     * @param setPresence     Controls whether the client is automatically marked as online by polling this API. If this parameter is
     *                        omitted then the client is automatically marked as online when it uses this API. Otherwise if the parameter
     *                        is set to "offline" then the client is not marked as being online when it uses this API. One of: ["offline"]
     * @param timeout         The maximum time to wait, in milliseconds, before returning this request. If no events (or other data) become
     *                        available before this time elapses, the server will return a response with empty fields.
     *                        <br>
     *                        By default, this is 0, so the server will return immediately even if the response is empty.
     * @param uriInfo         Request Information.
     * @param httpHeaders     Http headers.
     * @param asyncResponse   Asynchronous response.
     * @param securityContext Security context.
     */
    @Operation(
        summary = "Synchronise the client's state with the latest state on the server. Clients use this API when they first "
            + "log in to get an initial snapshot of the state on the server, and then continue to call this API to get incremental deltas "
            + "to the state, and to receive new messages.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The initial snapshot or delta for the client to use to update their state.",
                content = @Content(
                    schema = @Schema(
                        implementation = SyncResponse.class
                    )
                )
            )
        },
        security = {
            @SecurityRequirement(
                name = "accessToken"
            )
        },
        tags = {
            "Room participation"
        }
    )
    @GET
    @Secured
    @Path("/sync")
    void sync(
        @Parameter(
            description = "The ID of a filter created using the filter API or a filter JSON object encoded as a string. The server will "
                + "detect whether it is an ID or a JSON object by whether the first character is a \"{\" open brace. Passing the "
                + "JSON inline is best suited to one off requests. Creating a filter using the filter API is recommended for "
                + "clients that reuse the same filter multiple times, for example in long poll requests."
        ) @QueryParam("filter") String filter,
        @Parameter(
            description = "A point in time to continue a sync from."
        ) @QueryParam("since") String since,
        @Parameter(
            description = "Controls whether to include the full state for all rooms the user is a member of."
        ) @QueryParam("full_state") Boolean fullState,
        @Parameter(
            description = "Controls whether the client is automatically marked as online by polling this API. If this parameter is "
                + "omitted then the client is automatically marked as online when it uses this API. Otherwise if the parameter "
                + "is set to \"offline\" then the client is not marked as being online when it uses this API.",
            schema = @Schema(
                allowableValues = {
                    "offline"
                }
            )
        ) @QueryParam("set_presence") String setPresence,
        @Parameter(
            description = "The maximum time to wait, in milliseconds, before returning this request. If no events (or other data) become "
                + "available before this time elapses, the server will return a response with empty fields."
        ) @QueryParam("timeout") Long timeout,

        @Context UriInfo uriInfo,
        @Context HttpHeaders httpHeaders,
        @Suspended AsyncResponse asyncResponse,
        @Context SecurityContext securityContext
    );

    /**
     * This will listen for new events related to a particular room and return them to the caller. This will block until an event is
     * received, or until the timeout is reached.
     * <br>
     * This API is the same as the normal /events endpoint, but can be called by users who have not joined the room.
     * <br>
     * Note that the normal /events endpoint has been deprecated. This API will also be deprecated at some point, but its
     * replacement is not yet known.
     * <br>
     * <b>Requires auth</b>: Yes.
     * <br>
     * Return: {@link Page} of the {@link Event}s.
     * <p>Status code 200: The events received, which may be none.</p>
     * <p>Status code 400: Bad pagination from parameter.</p>
     *
     * @param from            The token to stream from. This token is either from a previous request to this API or from the initial sync
     *                        API.
     * @param timeout         The maximum time in milliseconds to wait for an event.
     * @param roomId          The room ID for which events should be returned.
     * @param uriInfo         Request Information.
     * @param httpHeaders     Http headers.
     * @param asyncResponse   Asynchronous response.
     * @param securityContext Security context.
     */
    @Operation(
        summary = "This will listen for new events related to a particular room and return them to the caller. "
            + "This will block until an event is received, or until the timeout is reached.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The events received, which may be none.",
                content = @Content(
                    schema = @Schema(
                        implementation = Page.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad pagination from parameter.",
                content = @Content(
                    schema = @Schema(
                        implementation = ErrorResponse.class
                    )
                )
            )
        },
        security = {
            @SecurityRequirement(
                name = "accessToken"
            )
        },
        tags = {
            "Room participation"
        }
    )
    @GET
    @Secured
    @Path("/events")
    void events(
        @Parameter(
            description = "The token to stream from. This token is either from a previous request to this API or from the initial sync API."
        ) @QueryParam("from") String from,
        @Parameter(
            description = "The maximum time in milliseconds to wait for an event."
        ) @QueryParam("timeout") Long timeout,
        @Parameter(
            description = "The room ID for which events should be returned."
        ) @QueryParam("room_id") String roomId,

        @Context UriInfo uriInfo,
        @Context HttpHeaders httpHeaders,
        @Suspended AsyncResponse asyncResponse,
        @Context SecurityContext securityContext
    );
}
