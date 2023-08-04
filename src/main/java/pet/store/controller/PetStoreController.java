package pet.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;
//import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
    private final PetStoreService petStoreService;

    @Autowired
    public PetStoreController(PetStoreService petStoreService) {
        this.petStoreService = petStoreService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
        
    	log.info("Creating pet store: {}", petStoreData);
        return petStoreService.savePetStore(petStoreData);
    }

    @PutMapping("{id}")
    public PetStoreData updatePetStore(@PathVariable Long id, @RequestBody PetStoreData petStoreData) {
    	petStoreData.setPetStoreId(id);
    	log.info("Updating pet store with ID {}", id, petStoreData);
        return petStoreService.updatePetStore(id, petStoreData);
    }
}