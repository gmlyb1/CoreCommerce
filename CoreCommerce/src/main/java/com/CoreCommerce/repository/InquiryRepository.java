package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.CoreCommerce.domain.Inquiry;
import com.CoreCommerce.domain.InquiryAnswer;
import com.CoreCommerce.domain.Member;

@Mapper
public interface InquiryRepository {

	List<Inquiry> findByMemberId(Inquiry inquiry);

	void saveInquiry(Inquiry inquiry);

	Inquiry findById(Long id);

	InquiryAnswer findAnswerByInquiryId(Long id);

	List<Inquiry> findAll();

	void saveAnswer(InquiryAnswer answer);

	void updateStatusToDone(Long inquiryId);

	void updateInquiry(Inquiry inquiry);

	void deleteInquiry(Long id);

	void updateAnswer(InquiryAnswer answer);

	void deleteAnswer(Long inquiryId);

}
