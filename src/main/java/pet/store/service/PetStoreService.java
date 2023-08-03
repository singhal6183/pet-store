package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class PetStoreService {

    private final PetStoreDao petStoreDao;

    @Autowired
    public PetStoreService(PetStoreDao petStoreDao) {
        this.petStoreDao = petStoreDao;
    }

    public PetStoreData savePetStore(PetStoreData petStoreData) {
        PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
        copyPetStoreFields(petStore, petStoreData);
        PetStore savedPetStore = petStoreDao.save(petStore);
        return new PetStoreData(savedPetStore);
    }

    public PetStoreData updatePetStore(Long petStoreId, PetStoreData petStoreData) {
        PetStore petStore = findPetStoreById(petStoreId);
        if (petStore == null) {
            throw new NoSuchElementException("Pet store not found with ID: " + petStoreId);
        }
        copyPetStoreFields(petStore, petStoreData);
        PetStore updatedPetStore = petStoreDao.save(petStore);
        return new PetStoreData(updatedPetStore);
    }

    private PetStore findOrCreatePetStore(Long petStoreId) {
        if (petStoreId == null) {
            return new PetStore();
        } else {
            Optional<PetStore> petStoreOptional = petStoreDao.findById(petStoreId);
            return petStoreOptional.orElseThrow(() -> new NoSuchElementException("Pet store not found with ID: " + petStoreId));
        }
    }

    private PetStore findPetStoreById(Long petStoreId) {
        Optional<PetStore> petStoreOptional = petStoreDao.findById(petStoreId);
        return petStoreOptional.orElse(null);
    }

    private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
        petStore.setPetStoreId(petStoreData.getPetStoreId());
        petStore.setPetStoreName(petStoreData.getPetStoreName());
        petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
        petStore.setPetStoreCity(petStoreData.getPetStoreCity());
        petStore.setPetStoreState(petStoreData.getPetStoreState());
        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
        petStore.setPetStorePhone(petStoreData.getPetStorePhone());

      /*  // Map customers from DTO to entity
        if (petStoreData.getCustomers() != null) {
            Set<Customer> customers = new HashSet<>();
            for (PetStoreData.PetStoreCustomer customerDTO : petStoreData.getCustomers()) {
                Customer customer = new Customer();
                customer.setPetStoreCustomerId(customerDTO.getPetStoreCustomerId());
                customer.setCustomerEmail(customerDTO.getCustomerEmail());
                customers.add(customer);
            }
            petStore.setCustomers(customers);
        } else {
            petStore.setCustomers(new HashSet<>());
        }*/

        // Map employees from DTO to entity
       /*if (petStoreData.getEmployees() != null) {
            Set<Employee> employees = new HashSet<>();
            for (PetStoreData.PetStoreEmployee employeeDTO : petStoreData.getEmployees()) {
                Employee employee = new Employee();
                employee.setId(employeeDTO.getId());
                employee.setEmployeeName(employeeDTO.getEmployeeName());
                employee.setPetStore(petStore);
                employees.add(employee);
            }
            petStore.setEmployees(employees);
        } else {
            petStore.setEmployees(new HashSet<>());
        }*/
    }
}