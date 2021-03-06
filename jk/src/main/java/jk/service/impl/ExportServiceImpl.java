
package jk.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jk.dao.ContractDao;
import jk.dao.ExportDao;
import jk.dao.ExportProductDao;
import jk.dao.ExtEproductDao;
import jk.domain.Contract;
import jk.domain.Export;
import jk.domain.ExportProduct;
import jk.domain.ExtCproduct;
import jk.domain.ExtEproduct;
import jk.pagination.Page;
import jk.service.ExportService;
import jk.util.UtilFuns;
import jk.vo.ContractProductVO;
import jk.vo.ContractVO;
import jk.vo.ExtCproductVO;

/**
 * @Description: TODO
 * @Author:	tyler
 * @Company:	
 * @CreateDate:	2017年2月9日
 */
@Service
public class ExportServiceImpl implements ExportService {

	@Resource
	ExportDao exportDao;
	
	@Resource
	ExportProductDao exportProductDao;
	
	@Resource
	ExtEproductDao extEproductDao;
	
	@Resource
	ContractDao contractDao;
	
	public List<Export> findPage(Page paramPage) {
		// TODO Auto-generated method stub
		return exportDao.findPage(paramPage);
	}

	public List<Export> find(Map paramMap) {
		// TODO Auto-generated method stub
		return exportDao.find(paramMap);
	}

	public Export get(Serializable id) {
		// TODO Auto-generated method stub
		return exportDao.get(id);
	}

	public void insert(String[] contractIds) {
		// TODO Auto-generated method stub
		/*
		 * 步骤：
		 * 1.根据合同id获得合同对象，获取合同号
		 * 2.将合同下的货物信息搬家到报运下的货物表中
		 * 3.将合同下的附件信息搬家到报运下的附件表中
		 * */
		
		//拼凑合同号 ，搬运号
		String contractNos = "";
		for (int i = 0; i < contractIds.length; i++) {
			ContractVO contract = contractDao.view(contractIds[i]);
			contractNos += contract.getContractNo() + " ";
		}
		UtilFuns.delLastChar(contractNos);
		Export export = new Export();
		export.setId(UUID.randomUUID().toString());
		export.setContractIds(UtilFuns.joinStr(contractIds, ","));
		export.setCustomerContract(contractNos);
		exportDao.insert(export);
		
		for (int i = 0; i < contractIds.length; i++) {
			ContractVO contract = contractDao.view(contractIds[i]);
			for (ContractProductVO cp : contract.getContractProducts()) {
				ExportProduct ep = new ExportProduct();
				ep.setExportId(export.getId());
				ep.setFactoryId(cp.getFactory().getId());
				ep.setFactoryName(cp.getFactory().getFactoryName());
				ep.setProductNo(cp.getProductNo());
				ep.setPackingUnit(cp.getPackingUnit());
				ep.setCnumber(cp.getCnumber());
				ep.setBoxNum(cp.getBoxNum());
				ep.setPrice(cp.getPrice());
				exportProductDao.insert(ep);
				
				//处理附件信息
				for(ExtCproductVO extcp : cp.getExtCproducts()){
					ExtEproduct extep = new ExtEproduct();
					//copyProperties spring
					BeanUtils.copyProperties(extcp, extep);		//spring工具类，数据的拷贝
					
					extep.setId(UUID.randomUUID().toString());
					extep.setExportProductId(ep.getId());		//绑定外键
					
					extep.setFactoryId(extcp.getFactory().getId());
					extep.setFactoryName(extcp.getFactory().getFactoryName());
					
					extEproductDao.insert(extep);
				}
			}
		}
		
	}

	public void update(Export export,String[] mr_id,
			Integer[] mr_orderNo,
			Integer[] mr_cnumber,
			Double[] mr_grossWeight,
			Double[] mr_netWeight,
			Double[] mr_sizeLength,
			Double[] mr_sizeWidth,
			Double[] mr_sizeHeight,
			Double[] mr_exPrice,
			Double[] mr_tax,
			Integer[] mr_changed) {
		
		exportDao.update(export);
		//批量修改货物信息
		for(int i=0;i<mr_id.length;i++){
			if(mr_changed[i]!=null && mr_changed[i]==1){			//修改标识，只有用户修改的行才进行更新
				ExportProduct ep = exportProductDao.get(mr_id[i]);
				
				ep.setOrderNo(mr_orderNo[i]);
				ep.setCnumber(mr_cnumber[i]);
				ep.setGrossWeight(mr_grossWeight[i]);
				ep.setNetWeight(mr_netWeight[i]);
				ep.setSizeLength(mr_sizeLength[i]);
				ep.setSizeWidth(mr_sizeWidth[i]);
				ep.setSizeHeight(mr_sizeHeight[i]);
				ep.setExPrice(mr_exPrice[i]);
				ep.setTax(mr_tax[i]);
				
				exportProductDao.update(ep);
			}
		}
	}

	public void deleteById(Serializable id) {
		// TODO Auto-generated method stub
		exportDao.deleteById(id);

	}

	public void delete(Serializable[] ids) {
		// TODO Auto-generated method stub

	}

	public void submit(Serializable[] ids) {
		// TODO Auto-generated method stub

	}

	public void cancel(Serializable[] ids) {
		// TODO Auto-generated method stub

	}

	public List<Contract> getContract() {
		Map map = new HashMap();
		map.put("state", 1);
		// TODO Auto-generated method stub
		return contractDao.find(map );
	}

}
