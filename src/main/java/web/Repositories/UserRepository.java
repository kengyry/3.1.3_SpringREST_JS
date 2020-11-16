package web.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLastName(String last_name);
}
