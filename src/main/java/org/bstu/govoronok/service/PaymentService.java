package org.bstu.govoronok.service;

import org.bstu.govoronok.model.Payment;
import org.bstu.govoronok.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id){
        return paymentRepository.findById(id);
    }

    public Payment getPaymentByName(String name){
        return paymentRepository.getPaymentByName(name);
    }
}
