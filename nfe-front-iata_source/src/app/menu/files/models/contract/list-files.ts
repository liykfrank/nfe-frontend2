
export interface Content {
	id: number;
	name: string;
	type: string;
	bytes: number;
	uploadDateTime: string;
	status: string;
}

export interface Sort {
	sorted: boolean;
	unsorted: boolean;
}

export interface Pageable {
	sort: Sort;
	offset: number;
	pageSize: number;
	pageNumber: number;
	unpaged: boolean;
	paged: boolean;
}

export interface Sort {
	sorted: boolean;
	unsorted: boolean;
}

export interface IListFiles {
	content: Content[];
	pageable: Pageable;
	last: boolean;
	totalPages: number;
	totalElements: number;
	size: number;
	number: number;
	sort: Sort;
	numberOfElements: number;
	first: boolean;
}
