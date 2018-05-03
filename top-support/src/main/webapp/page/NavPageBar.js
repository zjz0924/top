var paged_startRow = 0;
var paged_pageSize = 0;
var paged_total = 0;

var getFormById = function(id) {
        var node = document.getElementById(id);        
        var found = false;
        while (!found) {
            if (node.nodeName == 'FORM') {
                found = true;                
                return node;
            }
            node = node.parentNode;
        }
        return null;
    }

function getCurrentPage(){
	return parseInt(paged_startRow/paged_pageSize) + 1;
}

function getCurrentStart( currentPage ){
	return paged_pageSize * ( currentPage - 1 );
}

function getLastPage(){
	if (paged_total%paged_pageSize == 0){
		return parseInt( paged_total/paged_pageSize )
	} else {
		return parseInt( paged_total/paged_pageSize ) + 1;
	}
}
 

function submitForm(start,id){
	var form = getFormById(id);
	if(form.elements["startRow"].length>1) {
		form.elements["startRow"][0].value = start;
		form.elements["startRow"][1].value = start;
	} else {
		form.elements["startRow"].value   = start;	
	}
	
	if(typeof(beforProcessPageData) == 'function'){
	  beforProcessPageData();
	}	
	form.submit();
}

function init(id){
	var form = getFormById(id);
	if(form.elements["startRow"].length>1) {
		paged_startRow =form.elements["startRow"][0].value;
		paged_pageSize = form.elements["pageSize"][0].value;
		paged_total = form.elements["totalSize"][0].value;
	} else {
		paged_startRow =form.elements["startRow"].value;
		paged_pageSize = form.elements["pageSize"].value;
		paged_total = form.elements["totalSize"].value;
	}
}
    
function first_page(id){
	init(id);
	submitForm(0,id); 
}

function next_page(id){
	init(id);
	var start = getCurrentStart( getCurrentPage() + 1 );
	if( start < paged_total ){
		submitForm(start,id);
	}	
}

function last_page(id){
	init(id);
	var start = getCurrentStart( getLastPage() );
	submitForm(start,id);
}

function pre_page(id){
	init(id);
	var currentPage = getCurrentPage();
	if( currentPage > 1 ){
		var start = getCurrentStart( currentPage - 1 );
		submitForm(start,id);
	}	
}

function yygotoPage(id,obj){
	init(id);
	var form = getFormById(id);
	var page = 0;
	if(form.elements[obj].length>1) {
		page = form.elements[obj][0].value;
	}else{
		page = form.elements[obj].value;
	}
	if( page > 0 && page < ( getLastPage() + 1  ) ){
		start = getCurrentStart(page);
		submitForm(start,id);
	} else {
		alert("请输入介于1和"+ getLastPage() + "之间的数值。");
		return;
	}
}
function gopage(page,id){
	init(id);
	if( page > 0 && page < ( getLastPage() + 1  ) ){
		start = getCurrentStart(page);
		submitForm(start,id);
	}
}
function yyenterSubmit(e,id,obj){
	var key = window.event ? e.keyCode : e.which;
	if(key == 13){
		yygotoPage(id,obj);
	}else{
	   return;
	}
}

function nav_ready(fn){
	var loaded = false;
	function readyFunc() {
		if (!loaded) {
			loaded = true;
			fn();
		}
	}
	function ieReadyFunc() {
		if (!loaded) {
			try {
				document.documentElement.doScroll('left');
			} catch(e) {
				setTimeout(ieReadyFunc, 100);
				return;
			}
			readyFunc();
		}
	}
	function ieReadyStateFunc() {
		if (document.readyState === 'complete') {
			readyFunc();
		}
	}
	if (document.addEventListener) {
		nav_bind(document, 'DOMContentLoaded', readyFunc);
	} else if (document.attachEvent) {
		nav_bind(document, 'readystatechange', ieReadyStateFunc);
		var toplevel = false;
		try {
			toplevel = window.frameElement == null;
		} catch(e) {}
		if (document.documentElement.doScroll && toplevel) {
			ieReadyFunc();
		}
	}
	nav_bind(window, 'load', readyFunc);
}
function nav_bind(element, evts, func){// 添加事件
	element = element ||document;
    if (element.addEventListener){//firefox
         element.addEventListener(evts, func, false);
         return true;
    }else if(element.attachEvent){//ie
         var obj = element.attachEvent('on' + evts, func);
         return obj;
    }else{
	     element["on" + evts] = func;
	     return false;
    }
}
function nav_add(formid,btnid,val){	
	var frm = document.forms[formid];
	if(frm){
	   if(frm.elements[btnid]){//已存在
	   	 frm.elements[btnid].value = val;
	   }else{//不存在
	    var newInput = document.createElement("input");             
	    newInput.type="hidden";   
	    newInput.id=btnid;
	    newInput.name=btnid;
	    newInput.value=val;	
	    frm.appendChild(newInput);     
	   }
	}else{
	  alert("指定的Form不存在！");
	}  
}  
