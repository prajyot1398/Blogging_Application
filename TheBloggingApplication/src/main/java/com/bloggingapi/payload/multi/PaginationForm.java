package com.bloggingapi.payload.multi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationForm {
	
	private int pageSize;
	private int pageNum;
	
	private long totalPages;
	private long totalElements;
	
	private boolean isLastPage;
	
	private String sortColumn;
	private boolean sortAsc;

	public PaginationForm(String sortColumn, boolean sortAsc) {
		this.sortColumn = sortColumn;
		this.sortAsc = sortAsc;
	}
	 
	public static <T> PaginationForm getPaginationFormFromPage(Page<T> pageForm, String sortColumn, boolean sortAsc) {
		
		PaginationForm paginationForm = new PaginationForm(sortColumn, sortAsc);
		
		paginationForm.setPageNum(pageForm.getNumber());
		paginationForm.setPageSize(pageForm.getSize());
		paginationForm.setTotalElements(pageForm.getTotalElements());
		paginationForm.setTotalPages(pageForm.getTotalPages());
		paginationForm.setLastPage(pageForm.isLast());
		
		return paginationForm;
	}

	public static Pageable getPagination(Integer pageNum, Integer pageSize, String sortColumn, boolean sortAsc) {
		
		Sort sort = null;
		if(sortAsc) {
			sort = Sort.by(Sort.Direction.ASC, sortColumn);
		}else {
			sort = Sort.by(Sort.Direction.DESC, sortColumn);
		}
		
		Pageable pagination = PageRequest.of(pageNum, pageSize, sort);
		return pagination;
	}
	
}
