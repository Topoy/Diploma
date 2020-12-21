package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository <CaptchaCode, Integer>
{
   @Query(value = "INSERT INTO captcha_codes (code, secret_code, `time`) values ()", nativeQuery = true)
   void setCaptchaParameters();
}
