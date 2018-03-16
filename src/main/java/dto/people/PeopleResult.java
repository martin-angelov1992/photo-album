package dto.people;

import java.util.List;

public class PeopleResult {
	private int totalCount;
	private List<PersonResult> items;
	private boolean incompleteResults;

	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<PersonResult> getItems() {
		return items;
	}
	public void setItems(List<PersonResult> items) {
		this.items = items;
	}
	public boolean isIncompleteResults() {
		return incompleteResults;
	}
	public void setIncompleteResults(boolean incompleteResults) {
		this.incompleteResults = incompleteResults;
	}
}