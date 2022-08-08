package com.bloggingapi.payload.multi;

import lombok.Getter;

@Getter
public class PaginationWithContent<A> {
	
	private final A object;
	private final PaginationForm paginationForm;
	
	private PaginationWithContent(A object, PaginationForm paginationForm) {
		this.object = object;
		this.paginationForm = paginationForm;
	}
	
	public static <A> PaginationWithContent<A> of(A object, PaginationForm paginationForm) {
		return new PaginationWithContent<A>(object, paginationForm);
	}
}
