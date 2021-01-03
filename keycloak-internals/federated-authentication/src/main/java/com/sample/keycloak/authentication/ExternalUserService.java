/*
 * Copyright 2015 Smartling, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sample.keycloak.authentication;

import com.sample.keycloak.rest.FederatedUserModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Federated user service.
 *
 * @author Scott Rossillo
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ExternalUserService {
    @GET
    @Path("/auth-service/auth/v1/user/{username}")
    FederatedUserModel getUserDetails(@PathParam("username") String username);

    //FederatedUserModel getUserDetails(@HeaderParam("User-Agent") String userAgent, @PathParam("username") String username);

    /*@HEAD
    @Path("/auth-service/auth/v1/user/{username}/")
    Response validateUserExists(@PathParam("username") String username);

    @POST
    @Path("/auth-service/auth/v1/user/{username}/")
    Boolean validateLogin(@PathParam("username") String username, UserCredentialsDto passwordDto);*/
}
