package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.ClientRequest;
import com.sdtvnews.sdtvnews.dto.response.ClientResponse;
import com.sdtvnews.sdtvnews.entity.Client;
import com.sdtvnews.sdtvnews.repository.ClientRepository;
import com.sdtvnews.sdtvnews.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientRequest createClient(ClientRequest request) {
        // Check if the client already exists
        List<Client> existingCategories = clientRepository.findAll();
        for (Client client : existingCategories) {
            if (client.getName().equalsIgnoreCase(request.getName())) {
                // Return a message if the client already exists
                throw new CustomException("Client with the name '" + request.getName() + "' already exists.");
            }
        }

        // Create a new Client if it doesn't exist
        Client newClient = new Client();
        newClient.setName(request.getName());
        newClient.setPhoneNum(request.getPhoneNum());
        newClient.setDescription(request.getDescription());
        newClient.setStatus("1");//1=active;0=dis-active
        newClient.setCreateBy("");
        newClient.setCreateDate(LocalDateTime.now());

        // Save the new client
        clientRepository.save(newClient);

        return request; // or convert and return the saved entity as needed
    }

    @Override
    public List<ClientResponse> getAllClient() {
        List<Client> categories = clientRepository.findAll(); // Retrieve all categories from the repository
        List<ClientResponse> clientResponses = new ArrayList<>();

        for (Client client : categories) {
            ClientResponse response = new ClientResponse();
            response.setId(client.getId());
            response.setName(client.getName());
            response.setPhoneNum(client.getPhoneNum());
            response.setDescription(client.getDescription());
            response.setStatus(client.getStatus());
            response.setCreateDate(client.getCreateDate());
            // Set other necessary fields

            clientResponses.add(response);
        }

        return clientResponses;
    }

    @Override
    public Optional<ClientResponse> getClientById(Long id) {
        // Retrieve the client by its ID
        Optional<Client> clientOptional = clientRepository.findById(id);

        // Check if the client exists
        if (clientOptional.isPresent()) {
            // Convert Client to ClientResponse if it exists
            Client client = clientOptional.get();
            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setId(client.getId());
            clientResponse.setName(client.getName());
            clientResponse.setPhoneNum(client.getPhoneNum());
            clientResponse.setDescription(client.getDescription());
            clientResponse.setStatus(client.getStatus());
            clientResponse.setCreateDate(client.getCreateDate());
            // Set other necessary fields

            return Optional.of(clientResponse);
        } else {
            // Return an empty Optional if the client doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateClient(Long id, ClientRequest clientRequest) {
        // Find the client by its ID
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            // Update the client details
            client.setName(clientRequest.getName());
            client.setDescription(clientRequest.getDescription());
            client.setStatus(clientRequest.getStatus());
            client.setUpdateBy("");
            client.setUpdateDate(LocalDateTime.now());

            // Save the updated client
            clientRepository.save(client);
        } else {
            throw new CustomException("Client not found with ID: " + id);
        }
    }


    @Override
    public void updateClientStatus(Long id, String status) {
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setStatus(status); // Update the status (e.g., "0" for inactive)
            clientRepository.save(client); // Save the updated client
        } else {
            throw new CustomException("Client not found with ID: " + id);
        }
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public boolean isNameDuplicate(String name) {
        // Check if the title exists in the database
        return clientRepository.existsByName(name);
    }

}
