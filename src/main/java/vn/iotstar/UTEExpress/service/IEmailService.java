package vn.iotstar.UTEExpress.service;

public interface IEmailService {
    void sendOtp(String email, String otp);

	void sendRegisterOtp(String email, String otp);
}
