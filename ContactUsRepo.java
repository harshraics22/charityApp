package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.dto.ContactUs;

public interface ContactUsRepo extends JpaRepository<ContactUs , Integer> {
	
	

}
