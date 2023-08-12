package pet.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;
import java.util.List;
import java.util.Map;
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
    
    @PostMapping("/{id}/employee")
    @ResponseStatus(code = HttpStatus.CREATED)
    public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long id, @RequestBody PetStoreEmployee petStoreEmployee) {
    	log.info("Creating pet store employee {}", petStoreEmployee);
        return petStoreService.saveEmployee(id, petStoreEmployee);
    }
    
    @PostMapping("{id}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertCustomer(@PathVariable Long id, @RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Adding customer { } to pet store with ID=", petStoreCustomer, id);
		return petStoreService.saveCustomer(id, petStoreCustomer);
	}
    
    @GetMapping
	public List<PetStoreData> getAllPetStores() {
		
		log.info("Retrieving all pet stores");
	    List<PetStoreData> petStores = petStoreService.retrieveAllPetStores();
	    return petStores;
	}
	
	@GetMapping("{id}")
	public PetStoreData getPetStoreById(@PathVariable Long id) {
		
		log.info("Retrieving pet store with ID= {}", id);
		
	    PetStoreData petStore = petStoreService.retrievePetStoreById(id);
	    return petStore;
	}
	
	@DeleteMapping("{id}")
	public Map<String, String> deletePetStoreById(@PathVariable Long id) {
		log.info("Deleting pet store with ID={}", id);
		
	    petStoreService.deletePetStoreById(id);
	    return Map.of("message", "Deletion of pet store with ID=" + id + " was successful.");
	}
	

}