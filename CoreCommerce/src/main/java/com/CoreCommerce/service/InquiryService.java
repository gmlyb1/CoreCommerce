package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Inquiry;
import com.CoreCommerce.domain.InquiryAnswer;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
public class InquiryService {

	private final InquiryRepository inquiryRepository;
	
	public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }
	
	public List<Inquiry> findByMemberId(Inquiry inquiry) {
		return inquiryRepository.findByMemberId(inquiry);
	}
	
	public void saveInquiry(Inquiry inquiry) {
		inquiryRepository.saveInquiry(inquiry);
	}
	
	public Inquiry findById(Long id) {
		return inquiryRepository.findById(id);
	}
	
	public InquiryAnswer findAnswerByInquiryId(Long id) {
		return inquiryRepository.findAnswerByInquiryId(id);
	}
	
	public List<Inquiry> findAll() {
		return inquiryRepository.findAll();
	}
	
	public void saveAnswer(InquiryAnswer answer) {
		inquiryRepository.saveAnswer(answer);
	}
	
	public void updateStatusToDone(Long inquiryId) {
		inquiryRepository.updateStatusToDone(inquiryId);
	}
	
	public void updateInquiry(Inquiry inquiry) {
		inquiryRepository.updateInquiry(inquiry);
	}
	
	public void deleteInquiry(Long id) {
		inquiryRepository.deleteInquiry(id);
	}
	
	public void updateAnswer(InquiryAnswer answer) {
		inquiryRepository.updateAnswer(answer);
	}
	
	public void deleteAnswer(Long inquiryId) {
		inquiryRepository.deleteAnswer(inquiryId);
	}
	
	public List<Inquiry> findAllPaged(int offset, int size){
		return inquiryRepository.findAllPaged(offset, size);
	}
	
	public int countAll(){
		return inquiryRepository.countAll();
	}
	
	public int countByMemberId(String email){
		return inquiryRepository.countByMemberId(email);
	}
	
	public List<Inquiry> findByMemberIdPaged(String email, int offset, int size){
		return inquiryRepository.findByMemberIdPaged(email, offset, size);
	}
}
