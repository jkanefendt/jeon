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

import io.github.ma1uta.matrix.client.model.capability.CapabilitiesResponse;
import io.github.ma1uta.matrix.common.RateLimit;
import io.github.ma1uta.matrix.common.RateLimitedErrorResponse;
import io.github.ma1uta.matrix.common.Secured;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * A homeserver may not support certain operations and clients must be able to query for what the homeserver can and can't offer.
 * For example, a homeserver may not support users changing their password as it is configured to perform authentication against
 * an external system.
 */
@Path("/_matrix/client/r0")
@Produces(MediaType.APPLICATION_JSON)
public interface CapabilitiesApi {

    /**
     * Gets information about the server's supported feature set and other relevant capabilities.
     * <br>
     * <b>Requires auth</b>: Yes.
     * <br>
     * <b>Rate-limited</b>: Yes.
     * <br>
     * Return: {@link CapabilitiesResponse}.
     * <p>Status code 200: The capabilities of the server.</p>
     * <p>Status code 429: This request was rate-limited.</p>
     *
     * @param uriInfo       Request Information.
     * @param httpHeaders   Http headers.
     * @param asyncResponse Asynchronous response.
     */
    @Operation(
        summary = "Gets information about the server's supported feature set and other relevant capabilities.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The capabilities of the server.",
                content = @Content(
                    schema = @Schema(
                        implementation = CapabilitiesResponse.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "429",
                description = "This request was rate-limited.",
                content = @Content(
                    schema = @Schema(
                        implementation = RateLimitedErrorResponse.class
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
            "User data"
        }
    )
    @Secured
    @RateLimit
    @GET
    @Path("/")
    void capabilities(
        @Context UriInfo uriInfo,
        @Context HttpHeaders httpHeaders,
        @Suspended AsyncResponse asyncResponse
    );
}
