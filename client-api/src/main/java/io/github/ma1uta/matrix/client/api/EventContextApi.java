/*
 * Copyright sablintolya@gmail.com
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

import static io.github.ma1uta.matrix.client.api.EventContextApi.PATH;

import io.github.ma1uta.matrix.Secured;
import io.github.ma1uta.matrix.client.model.eventcontext.EventContextResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * This API returns a number of events that happened just before and after the specified event. This allows clients to get the context
 * surrounding an event.
 * <p/>
 * <a href="https://matrix.org/docs/spec/client_server/r0.3.0.html#id114">Specification.</a>
 */
@Api(value = PATH, description = "This API returns a number of events that happened just before and after the specified event. "
    + "This allows clients to get the context surrounding an event.")
@Path(PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface EventContextApi {

    /**
     * Event context api url.
     */
    String PATH = "/_matrix/client/r0/rooms";

    /**
     * This API returns a number of events that happened just before and after the specified event. This allows clients to get the
     * context surrounding an event.
     * <p/>
     * <b>Requires auth</b>: Yes.
     *
     * @param roomId          Required. The room to get events from.
     * @param eventId         Required. The event to get context around.
     * @param limit           The maximum number of events to return. Default: 10.
     * @param servletRequest  servlet request.
     * @param servletResponse servlet response.
     * @param securityContext security context.
     * @return Status code 200: The events and state surrounding the requested event.
     */
    @ApiOperation(value = "This API returns a number of events that happened just before and after the specified event.",
        notes = "This allows clients to get the context surrounding an event.")
    @ApiResponses( {
        @ApiResponse(code = 200, message = "The events and state surrounding the requested event.")
    })
    @GET
    @Secured
    @Path("/{roomId}/context/{eventId}")
    EventContextResponse context(
        @ApiParam(value = "The room to get events from.", required = true) @PathParam("roomId") String roomId,
        @ApiParam(value = "The event to get context around.", required = true) @PathParam("eventId") String eventId,
        @ApiParam("The maximum number of events to return. Default: 10.") @QueryParam("limit") Integer limit,
        @Context HttpServletRequest servletRequest, @Context HttpServletResponse servletResponse, @Context SecurityContext securityContext);
}
