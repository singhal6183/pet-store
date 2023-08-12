package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.PetStoreDao;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.LinkedList;
import java.util.List;

@Service
public class PetStoreService {

    private final PetStoreDao petStoreDao;
    private final EmployeeDao employeeDao;
	private final CustomerDao customerDao;

    @Autowired
    public PetStoreService(PetStoreDao petStoreDao, EmployeeDao employeeDao, CustomerDao customerDao) {
        this.petStoreDao = petStoreDao;
        this.employeeDao = employeeDao;
		this.customerDao = customerDao;
    }
    
    // for PetStore data
   // @Transactional(readOnly = false)
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
    // For Employee data
    @Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		
		Employee dbEmployee = employeeDao.save(employee);
		
		return new PetStoreEmployee(dbEmployee);
	}
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if(employeeId == null) {
			return new Employee();
		} 
		return findEmployeeById(petStoreId, employeeId);
	}


	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		 Employee employee = employeeDao.findById(employeeId).orElseThrow(
				 () -> new NoSuchElementException(
				 "Employee with ID=" + employeeId + " was not found."));
		 
		 if(employee.getPetStore().getPetStoreId() != petStoreId) {
			 throw new IllegalArgumentException("The employee with ID=" + employeeId 
					 + " is not employed by the pet store with ID=" + petStoreId + ".");
		 }
		 return employee;
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
	}
	
	// for Customer data
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		
		copyCustomerFields(customer, petStoreCustomer);
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		
		Customer dbCustomer = customerDao.save(customer);
		
		return new PetStoreCustomer(dbCustomer);
	}
	
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(customerId == null) {
			return new Customer();
		} 
		return findCustomerById(petStoreId, customerId);
	}


	private Customer findCustomerById(Long petStoreId, Long customerId) {
		 Customer customer = customerDao.findById(customerId).orElseThrow(
				 () -> new NoSuchElementException(
				 "Customer with ID=" + customerId + " was not found."));
		 
		 boolean found = false;
		 
		 for(PetStore petStore : customer.getPetStores()) {
			 if(petStore.getPetStoreId() == petStoreId) {
				 found = true;
				 break;
			 }
		 }
		 if(!found) {
			 throw new IllegalArgumentException("The customer with ID=" + customerId 
					 + " is not a member of the pet store with ID=" + petStoreId + ".");
		 }
		 return customer;
	}
	
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}
	
	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		
		List<PetStoreData> response = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			
			response.add(psd);
		}
		return response;
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		return new PetStoreData(findPetStoreById(petStoreId));
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}

}