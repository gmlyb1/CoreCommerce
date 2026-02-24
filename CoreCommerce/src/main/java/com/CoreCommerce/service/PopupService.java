package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Popup;
import com.CoreCommerce.repository.PopupRepository;

@Service
public class PopupService {

	private final PopupRepository popupRepository;
	
	public PopupService(PopupRepository popupRepository) {
        this.popupRepository = popupRepository;
    }
	
	public void save(Popup popup) {
		popupRepository.save(popup);
	}

	public List<Popup> findAll() {
		return popupRepository.findAll();
	}

	public Popup findById(Long id) {
		return popupRepository.findById(id);
	}

	public void update(Popup popup) {
		popupRepository.update(popup);
	}

	public void delete(Long id) {
		popupRepository.delete(id);
	}

	public Popup findActivePopup() {
		return popupRepository.findActivePopup();
	}
	
	public int countAll() {
	    return popupRepository.countAll();
	}

	public List<Popup> findPaging(int offset, int size) {
	    return popupRepository.findPaging(offset, size);
	}
	
}
