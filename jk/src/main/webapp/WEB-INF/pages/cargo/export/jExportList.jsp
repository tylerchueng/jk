<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
</head>

<body>
	<form name="icform" method="post">

		<div id="menubar">
			<div id="middleMenubar">
				<div id="innerMenubar">
					<div id="navMenubar">
						<ul>
							<li id="view"><a href="#"
								onclick="formSubmit('toview.action','_self');this.blur();">查看</a></li>

							<li id="update"><a href="#"
								onclick="formSubmit('toupdate.action','_self');this.blur();">修改</a></li>
							<li id="delete"><a href="#"
								onclick="formSubmit('delete.action','_self');this.blur();">删除</a></li>
							<li id="new"><a href="#"
								onclick="formSubmit('submit.action','_self');this.blur();">上报</a></li>
							<li id="new"><a href="#"
								onclick="formSubmit('cancel.action','_self');this.blur();">取消</a></li>
							<li id="new"><a href="#"
								onclick="formSubmit('${ctx}/cargo/packinglist/tocreate.action','_self');this.blur();">装箱</a></li>

						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class="textbox" id="centerTextbox">
			<div class="textbox-header">
				<div class="textbox-inner-header">
					<div class="textbox-title">出口报运列表</div>
				</div>
			</div>

			<div>

				<div class="eXtremeTable">
					<table id="ec_table" class="tableRegion" width="98%">
						<thead>
							<tr>
								<td class="tableHeader"><input type="checkbox" name="selid"
									onclick="checkAll('id',this)"></td>
								<td class="tableHeader">序号</td>
								<td class="tableHeader">合同或确认书号</td>
								<td class="tableHeader">信用证号</td>
								<td class="tableHeader" align="right">货物/附件</td>
								<td class="tableHeader">收货人及地址</td>
								<td class="tableHeader">装运港</td>
								<td class="tableHeader">目的港</td>
								<td class="tableHeader">运输方式</td>
								<td class="tableHeader">价格条件</td>
								<td class="tableHeader">制单日期</td>
								<td class="tableHeader">状态</td>
								<td class="tableHeader">操作</td>
							</tr>
						</thead>
						<tbody class="tableBody">

							<c:forEach items="${dataList}" var="o" varStatus="status">
								<tr class="odd" onmouseover="this.className='highlight'"
									onmouseout="this.className='odd'">
									<td><input type="checkbox" name="id" value="${o.id}" /></td>
									<td>${status.index+1}</td>
									<td><a href="toview.action?id=${o.id}">${o.customerContract}</a></td>
									<td>${o.lcno}</td>
									<td align="center">${o.epnum}/${o.extnum}</td>
									<td>${o.consignee}</td>
									<td>${o.shipmentPort}</td>
									<td>${o.destinationPort}</td>
									<td>${o.transportMode}</td>
									<td>${o.priceCondition}</td>
									<td><fmt:formatDate value="${o.inputDate}"
											pattern="yyyy-MM-dd" /></td>
									<td><c:if test="${o.state==1}">
											<font color="green">已上报</font>
										</c:if> <c:if test="${o.state==0}">草稿</c:if></td>
									<td><a
										href="${ctx}/cargo/contractproduct/tocreate.action?contractId=${o.id}"
										title="新增货物信息">[货物]</a></td>
								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>

			</div>
	</form>
</body>
</html>

