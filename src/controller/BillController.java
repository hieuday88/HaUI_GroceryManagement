package controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import dao.BillDAO;
import model.Bill;
import view.BillManagement;

public class BillController {
	private BillDAO billDAO;
	private BillManagement bm;

	public BillController(BillDAO billDAO, BillManagement bm) {
		this.billDAO = billDAO;
		this.bm = bm;
	}

	public List<Bill> getAllBills() throws ClassNotFoundException, IOException {
		return billDAO.getAll();
	}

	public Bill getBillById(String id) throws ClassNotFoundException, IOException {
		return billDAO.get(Bill -> Bill.getId().equals(id));
	}

	public boolean addBill(Bill Bill) throws ClassNotFoundException, IOException {
		if (!billDAO.add(Bill)) {
			return false;
		}
		if (bm != null)
			bm.updateBillTable(getAllBills());
		return true;
	}

	public boolean updateBill(Bill Bill) throws ClassNotFoundException, IOException {
		if (!billDAO.update(Bill)) {
			return false;
		}
		if (bm != null)
			bm.updateBillTable(getAllBills());
		return true;
	}

	public boolean deleteBill(Bill Bill) throws ClassNotFoundException, IOException {
		if (!billDAO.delete(Bill)) {
			return false;
		}
		if (bm != null)
			bm.updateBillTable(getAllBills());
		return true;
	}

	public void searchBills(List<Bill> bills, String keyword, int searchOption) {
		List<Bill> searchedBills;
		if (searchOption == 0)
			searchedBills = bills.stream().filter(bill -> bill.getId().contains(keyword)).collect(Collectors.toList());
		else
			searchedBills = bills.stream().filter(bill -> bill.getDate().contains(keyword))
					.collect(Collectors.toList());
		if (bm != null)
			bm.updateBillTable(searchedBills);
	}

}
