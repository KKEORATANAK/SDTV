package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.ClientRequest;
import com.sdtvnews.sdtvnews.dto.response.ClientResponse;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientRequest createClient(ClientRequest request);

    List<ClientResponse>getAllClient();

    Optional<ClientResponse>getClientById(Long id);

    void updateClient(Long id, ClientRequest categoryRequest);

    void updateClientStatus(Long id, String status);

    void deleteClient(Long id);

    boolean isNameDuplicate(String name);
}
