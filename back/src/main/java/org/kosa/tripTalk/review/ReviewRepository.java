package org.kosa.tripTalk.review;

import java.util.List;

import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
}
