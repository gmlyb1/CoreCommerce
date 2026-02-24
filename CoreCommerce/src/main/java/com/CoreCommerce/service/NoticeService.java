package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Notice;
import com.CoreCommerce.repository.NoticeRepository;

@Service
public class NoticeService {

	private final NoticeRepository noticeRepository;
	
	public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }
	
	public List<Notice> findAll() {
		return noticeRepository.findAll();
	}

	public Notice findById(Long id) {
		return noticeRepository.findById(id);
	}

	public void insert(Notice notice) {
		noticeRepository.insert(notice);
	}

	public List<Notice> findPaged(int page, int size) {
		return noticeRepository.findPaged(page,size);
	}

	public int countAll() {
		return noticeRepository.countAll();
	}

	public void update(Notice notice) {
		noticeRepository.update(notice);
	}

	public void delete(Long id) {
		noticeRepository.delete(id);
	}

}
