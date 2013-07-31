<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/extjs3.4/resources/css/ext-all.css" />

<script type="text/javascript" src="${pageContext.request.contextPath }/js/extjs3.4/ext-base.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/extjs3.4/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/extjs3.4/TabCloseMenu.js"></script>
</head>
<body>
  
</body>

<script type="text/javascript" >
	
Ext.onReady(function(){
	var grid = new Ext.grid.GridPanel({
	    store: new Ext.data.Store({
	        autoDestroy: true,
	        reader: reader,
	        data: xg.dummyData
	    }),
	    colModel: new Ext.grid.ColumnModel({
	        defaults: {
	            width: 120,
	            sortable: true
	        },
	        columns: [
	            {id: 'company', header: 'Company', width: 200, sortable: true, dataIndex: 'company'},
	            {header: 'Price', renderer: Ext.util.Format.usMoney, dataIndex: 'price'},
	            {header: 'Change', dataIndex: 'change'},
	            {header: '% Change', dataIndex: 'pctChange'},
	            // instead of specifying renderer: Ext.util.Format.dateRenderer('m/d/Y') use xtype
	            {
	                header: 'Last Updated', width: 135, dataIndex: 'lastChange',
	                xtype: 'datecolumn', format: 'M d, Y'
	            }
	        ]
	    }),
	    viewConfig: {
	        forceFit: true,

//	      Return CSS class to apply to rows depending upon data values
	        getRowClass: function(record, index) {
	            var c = record.get('change');
	            if (c < 0) {
	                return 'price-fall';
	            } else if (c > 0) {
	                return 'price-rise';
	            }
	        }
	    },
	    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	    width: 600,
	    height: 300,
	    frame: true,
	    title: 'Framed with Row Selection and Horizontal Scrolling',
	    iconCls: 'icon-grid'
	});
});

</script>
</html>