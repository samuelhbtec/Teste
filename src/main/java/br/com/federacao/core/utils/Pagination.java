package br.com.federacao.core.utils;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination<T> {

	private List<T> items;
	
	private Integer page;

	private Integer pageSize;
	
	private Integer pageLast;
	
	private Integer pageStart;
	
	private Integer pageEnd;
	
	private Integer countResults = 0;

	public int getFirstResult() {
		return pageSize * (page - 1);
	}

	public Pagination() {
		this(1,10);
	}
	
	public Pagination(Integer page, Integer pageSize) {
		super();
		
		if(page == null)
			this.page = 1;
		this.page = page;
		
		if(pageSize == null)
			this.pageSize = 10;
		this.pageSize = pageSize;
	}
	
	public Integer getPageLast() {

		if (pageLast == null)
			pageLast = (int) Math.ceil(this.countResults.doubleValue() / this.pageSize.doubleValue());

		return pageLast;
	}

	public void setPageLast(Integer pageLast) {
		this.pageLast = pageLast;
	}
	
	public void setPageStart(Integer pageStart){
		this.pageStart = pageStart;
	}
	
	public Integer getPageStart(){
		this.pageStart = (pageSize * (page - 1)) + 1;
		return this.pageStart;
	}
	
	public void setPageEnd(Integer pageEnd){
		this.pageEnd = pageEnd;
	}
	
	public Integer getPageEnd(){
		this.pageEnd = pageSize * (page);
		if(pageEnd >= this.countResults)
			return this.countResults;
		return this.pageEnd;
	}

}
