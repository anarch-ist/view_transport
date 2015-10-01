<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>Создание нового документа</title>
	<link rel="stylesheet" type="text/css" href="./style.css">
	<link rel="stylesheet" type="text/css" href="../../common_files/protoplasm.css">
	<link rel="stylesheet" type="text/css" href="../../common_files/datepicker/datepicker.css">
	<link rel="stylesheet" type="text/css" href="../../common_files/timepicker/timepicker.css">
	<script type="text/javascript" src="../../common_files/JSfunctions.js"></script>
	<script type="text/javascript" src="../../common_files/sessvars.js"></script>
	<script type="text/javascript" src="../../common_files/json.js"></script>
	<script type="text/javascript" src="../../common_files/protoplasm.js"></script>
<script type="text/javascript">
	//variables' block
	var navigation;
	var objectForClientsTable;
	var docDataClass;
	var objectsForTree;
	var objectsForRequest;
	var objectsForInsideRequestMain;
	var objectsForInsideRequest;
	var objectsInvoice;
	var objectsInvoiceMain;
	var requestButtons;
	var insideRequestButtons;
	var invoiceButtons;
	var docInformation = new Array();
	//classes' block
	var DocInformation = function(number) {
		var base = this;
		this.number = number;
		this.docView = new function() {			//element for viewing document info
			this.panel = document.getElementsByClassName('content-document')[base.number].getElementsByClassName('documentInfo')[0];
			this.fieldNames = new Array();
			switch (base.number) {
				case 0: {
					this.fieldNames[0]='Номер заявки';
					this.fieldNames[1]='Дата создания';
					this.fieldNames[2]='Изменен';
					break;
				}
				case 1: {
					this.fieldNames[0]='Номер внутренней заявки';
					this.fieldNames[1]='Дата создания';
					this.fieldNames[2]='Склад';
					this.fieldNames[3]='Изменен';
					break;
				}
				case 2: {
					this.fieldNames[0]='Номер накладной';
					this.fieldNames[1]='Дата создания';
					this.fieldNames[2]='открытие накладной';
					this.fieldNames[3]='закрытие накладной';
					this.fieldNames[4]='Изменен';
					break;
				}
			}
			this.hidePanel = function() {
				base.docView.panel.className = "documentInfo";
				base.docEdit.panel.className += " selected";
			}
			this.showPanel = function() {
				base.docView.panel.className += " selected";
				base.docEdit.panel.className = "documentEdit";
			}
			this.enableObjects = function() {
				requestButtons.enableButtons();
				insideRequestButtons.enableButtons();
				invoiceButtons.enableButtons();
				navigation.unlock();
			}
			this.fillData = function(data) {
				fieldset = this.panel.getElementsByClassName('viewElement');
				for (var i=0;i<fieldset.length;i++) {
					fieldset[i].getElementsByTagName('span')[1].removeChild(fieldset[i].getElementsByTagName('span')[1].firstChild);
					fieldset[i].getElementsByTagName('span')[1].appendChild(document.createTextNode(data[i]));
				}
			}
		}
		this.docEdit = new function() {			//element for document creating
			this.panel = document.getElementsByClassName('content-document')[base.number].getElementsByClassName('documentEdit')[0];
			this.fields = document.getElementsByClassName('content-document')[base.number].getElementsByClassName('documentEdit')[0].getElementsByTagName('label');
			this.button = this.panel.getElementsByClassName('button-submit')[0].getElementsByTagName('input')[0];
			this.cancel = this.panel.getElementsByClassName('button-submit')[0].getElementsByTagName('button')[0];
			this.cancel.onclick = function() {
				base.docView.showPanel();
				base.docView.enableObjects();
				cancelClick(event || window.event);
				synchronizedClassImpl.freemutex();
			}
			this.button.onclick = function() {
				cancelClick(event || window.event);
				base.docView.enableObjects();
				var data = new Object();
				for (var i=0;i<base.docEdit.fields.length;i++) {
					if (!base.docEdit.fields[i].getElementsByTagName('input')[0]) {
						data[base.docEdit.fields[i].getElementsByTagName('select')[0].name] = base.docEdit.fields[i].getElementsByTagName('select')[0].value;
					}
					else {
						data[base.docEdit.fields[i].getElementsByTagName('input')[0].name] = base.docEdit.fields[i].getElementsByTagName('input')[0].value;
					}
				}
				docDataClass.setNewDocData(base.number, data);
				base.docView.showPanel();
			}
		}
	}
	
	var synchronizedClassImpl = new function() {								// single object for synchronization of site's streams
		this.mutex = false;
		this.activatemutex = function() {
			this.mutex = true;
		}
		this.freemutex = function() {
			this.mutex = false;
		}
		this.getmutex = function() {
			return this.mutex;
		};
	};

	var DocDataClass = function() {
		this.currentClient;														// ID of selected client
		this.currentRequestObj;													//object request for keep its data
		this.currentInsideRequestObj;											//object inside request for keep its data
		this.currentInvoiceObj;													// ID of selected invoice
		this.docTree;															// Contains all docs of current client
		var base = this;
		this.setCurrentClient = function(currentClient) {						// Set client and get document information, write it in docTree, get  requests and output them in fields
			this.currentClient = currentClient;
			base.currentRequestObj = null;
			base.currentInsideRequestObj = null;
			base.currentInvoiceObj = null;
			this.getDocumentsOfClient(0);
		}
		this.setCurrentRequest = function(currentRequest) {										// Set request, get inside requests and output them in fields
			var baseObj = currentRequest;
			if (typeof(baseObj) ==='object') {
				docInformation[0].docView.fillData(new Array(baseObj.documentNumber,baseObj.dat,baseObj.marketAgent));
				base.currentInsideRequestObj = null;
				base.currentInvoiceObj = null;
				this.currentRequestObj = new function() {
					this.currentRequest = baseObj.documentID;										// ID of selected request
					this.requestNumber = baseObj.documentNumber;									// ID of selected request
					this.creatingTime = baseObj.dat;												// Datetime of request creating
					this.createdBy = baseObj.marketAgent;											// Marketman who created this request
				}
			} else if (typeof(baseObj) ==='string') {
				this.currentRequestObj = new function() {
					this.currentRequest = baseObj;													// ID of selected request
				}
				this.getData(0, baseObj);
			}
		}
		this.setCurrentInsideRequest = function(currentInsideRequest) {
			var baseObj = currentInsideRequest;
			if (typeof(baseObj) === 'object') {
				docInformation[1].docView.fillData(new Array(baseObj.documentNumber,baseObj.warehouse,baseObj.dat,baseObj.LastModBy));
				base.currentInvoiceObj = null;
				this.currentInsideRequestObj = new function() {
					this.currentInsideRequest = baseObj.documentID;									// ID of selected inside request
					this.insideRequestNumber = baseObj.documentNumber;								// ID of selected inside request
					this.creatingTime = baseObj.dat;												// Datetime of inside request creating
					this.warehouse = baseObj.warehouse;												// Warehouse for inside request
					this.createdBy = baseObj.LastModBy;												// Marketman who created this inside request
				}
			} else if (typeof(baseObj) ==='string') {
				this.currentInsideRequestObj = new function() {
					this.currentInsideRequest = baseObj;											// ID of selected inside request
				}
				this.getData(1, baseObj);
			}
		}
		this.setCurrentInvoice = function(currentInvoice) {
			var baseObj = currentInvoice;
			if (typeof(baseObj) === 'object') {
				//base.currentInvoiceObj = null;
				docInformation[2].docView.fillData(new Array(baseObj.documentNumber,baseObj.dat,baseObj.startT,baseObj.endT,baseObj.LastModBy));
				this.currentInvoiceObj = new function() {
					this.currentInvoice = baseObj.documentID;										// ID of selected invoice
					this.invoiceNumber = baseObj.documentNumber;									// ID of selected invoice
					this.creatingTime = baseObj.dat;												// Datetime of invoice creating
					this.tStart = baseObj.startT;													// открытие накладной?
					this.tEnd = baseObj.endT;														// закрытие накладной?
					this.lastModBy = baseObj.LastModBy;												// Marketman who created this invoice
				}
			} else if (typeof(baseObj) ==='string') {
				this.currentInvoiceObj = new function() {
					this.currentInvoice = baseObj;													// ID of selected invoice
				}
				this.getData(2, baseObj);
			}
		}
		var igetDocumentsOfClient=0;
		var isetNewDocData=0;
		var igetData = 0;
		this.getDocumentsOfClient = function(number) {
			var getDocs = new XMLHttpRequest();
			getDocs.open('post', 'documents.php', true);
			getDocs.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			getDocs.send('ClientID='+this.currentClient+'&Status=getDocs');
			console.log('getting information using ajax..');
			console.log(number);
			synchronizedClassImpl.activatemutex();
			getDocs.onreadystatechange = function() {
				if (this.readyState!=4) return;
				docDataClass.docTree = JSON.parse(this.responseText)[0];
				console.log('information was obtained.');
				objectsForTree.buildTree();
				objectsForRequest.buildList();
				console.log(number);
				if (number>0) {
					objectsForInsideRequestMain.buildList();
					objectsForInsideRequest.buildList();
				}
				if (number>1) {
					objectsInvoice.buildList();
					objectsInvoiceMain.buildList();
				}
				synchronizedClassImpl.freemutex();
			}
			console.log('number igetDocumentsOfClient ' + ++igetDocumentsOfClient);
			console.log('sending data ' + 'ClientID='+this.currentClient+'&Status=getDocs');
		}
		this.setNewDocData = function(number, params) {									// TODO: add new request with data from form and without getDocumentsOfClient() 
			number--;
			newQuery = new XMLHttpRequest();
			newQuery.open('post', 'documents.php', true);
			newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			var status;
			switch (number) {
				case 0: {
					status = 'addNewRequest';
					break;
				}
				case 1: {
					status = 'addNewInsideRequest&request='+docDataClass.currentRequestObj.currentRequest;
					break;
				}
				case 2: {
					status = 'addNewInvoice&insideRequest='+docDataClass.currentInsideRequestObj.currentInsideRequest;
					break;
				}
			};
			for (var i in params) {
				status += '&'+i+'='+params[i];
			}
			newQuery.send('ClientID='+this.currentClient+'&Status='+status);
			newQuery.onreadystatechange = function() {
				if (newQuery.readyState!=4) return;
				docDataClass.getDocumentsOfClient(number);
				switch (number) {
					case 0: {
						base.setCurrentRequest(JSON.parse(this.responseText));
						break;
					}
					case 1: {
						base.setCurrentInsideRequest(JSON.parse(this.responseText));
						break;
					}
					case 2: {
						base.setCurrentInvoice(JSON.parse(this.responseText));
						break;
					}
				};
				console.log(JSON.parse(this.responseText));
				// document.getElementsByClassName(this.responseText)[0].onclick();
			}
			console.log('number isetNewDocData ' + ++isetNewDocData);
			console.log('sending data ' + 'ClientID='+this.currentClient+'&Status='+status);
		}
		this.getData = function(number, baseObj) {
			var remLater = ++igetData;
			newQuery = new XMLHttpRequest();
			newQuery.open('post', 'documents.php', true);
			newQuery.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			var status='';
			switch (number) {
				case 0: {
					status = 'getRequest';
					break;
				}
				case 1: {
					status = 'getInsideRequest';
					break;
				}
				case 2: {
					status = 'getInvoice';
					break;
				}
			};
			status+='&DocumentID='+baseObj;
			newQuery.send('ClientID='+this.currentClient+'&Status='+status);
			newQuery.onreadystatechange = function() {
				if (newQuery.readyState!=4) return;
				console.log('number igetData ' + remLater);				
				if (this.responseText!=='') {
					switch (number) {
						case 0: {
							base.setCurrentRequest(JSON.parse(this.responseText));
							break;
						}
						case 1: {
							base.setCurrentInsideRequest(JSON.parse(this.responseText));
							break;
						}
						case 2: {
							base.setCurrentInvoice(JSON.parse(this.responseText));
							break;
						}
					};
					console.log(JSON.parse(this.responseText));
					//document.getElementsByClassName(JSON.parse(this.responseText).documentID)[0].onclick();
				}
			}
			console.log('number igetData ' + igetData);
			console.log('sending data ' + 'ClientID='+this.currentClient+'&Status='+status);
		};
	};
	
	var DOMObjectsForNavigation = function() {									// class for navigation
		this.allTabs = document.getElementById('content-document-menu').getElementsByTagName('ul')[0].children;			// tabs' array;
		this.currentTabNumber = 0;												// current tab number;
		this.maxNumber = 0;
		this.allTabs[this.currentTabNumber].className = 'selected';
		this.lock = function() {
			for (var i=0;i<this.allTabs.length;i++) {
				this.allTabs[i].className = 'disabled';
			}
			this.goToTab(this.currentTabNumber);
		}
		this.unlock = function() {
			this.switchOnTab(this.maxNumber);
		}
		this.switchOnTab = function(number) {
			this.maxNumber = number;
			for (var i=0;i<this.allTabs.length;i++) {
				if (i>number) {
					this.allTabs[i].className = 'disabled';
				}
				else {
					this.allTabs[i].removeAttribute('class');
				}
			}
			this.goToTab(this.currentTabNumber);
		}
		this.contentDocuments = document.getElementsByClassName('content-document');
		this.contentDocument = this.contentDocuments[0];
		this.contentDocument.className += ' selected';
		this.goToTab = function(number) {
			this.allTabs[this.currentTabNumber].removeAttribute('class');
			this.contentDocument.className = 'content-document';
			this.currentTabNumber = number;
			this.allTabs[this.currentTabNumber].className = 'selected';
			this.contentDocument = base.contentDocuments[number];
			this.contentDocument.className += ' selected';
		}
		this.allTabs.indexOf = function(base) {
			for (var i in this) {
				if (this[i] === base)
					return i;
			}
			return -1;
		}
		for (var i in this.allTabs) {
			var base = this;
			if (this.allTabs[i]!==this.allTabs[this.currentTabNumber]) {
				this.allTabs[i].className = 'disabled';
			}
			this.allTabs[i].onclick = function() {							// switches tabs
				if (this!==base.currentTab && this.className!=='disabled') {
					base.goToTab(base.allTabs.indexOf(this));
				}
			}
		}
	};
	
	var DocButton = function(levelOfDoc) {										// class for set behavior for 'add'-buttons
		this.button = document.getElementsByClassName('content-document')[levelOfDoc].getElementsByClassName('formClass')[0].getElementsByTagName('button')[0];
		this.buttonMain = document.getElementsByClassName('content-document')[levelOfDoc-1].getElementsByClassName('formClass')[levelOfDoc==1?0:1].getElementsByTagName('button')[0];
		this.level = levelOfDoc;
		var base = this;
		this.buttonMain.onclick = function(event) {
			base.clicked(event);
		};
		this.button.onclick = function(event) {
			base.clicked(event);
		};
		this.clicked = function(event) {
			if (!synchronizedClassImpl.getmutex()) {
				navigation.goToTab(this.level);
				requestButtons.disableButtons();
				insideRequestButtons.disableButtons();
				invoiceButtons.disableButtons();
				navigation.lock();
				synchronizedClassImpl.activatemutex();
				docInformation[base.level-1].docView.hidePanel();
			}
			cancelClick(event || window.event);
		}
		this.disableButtons = function() {
			this.buttonMain.style.display = 'none';
			this.button.style.display = 'none';
		}
		this.enableButtons = function() {
			this.buttonMain.removeAttribute('style');
			this.button.removeAttribute('style');
		}
	};
	
	var DOMObjectsForClientsTable = function() {								// class for working with clients' table
		this.currentRow=null;																						// current row in user table;
		this.table = document.getElementById('userTable').getElementsByTagName('table')[0];							// clients' table
		this.allRows = document.getElementById('userTable').getElementsByTagName('tr');								// rows' array in clients'table;
		this.noTable = document.getElementById('noTable');															// text with node "no clients"
		this.displayTable = function(count) {
			if (!count) {
				count = base.allRows.length;
			}
			if (count < 2) {
				this.noTable.removeAttribute('style');
				this.table.style.display = 'none';
			}
			else {
				this.table.removeAttribute('style');
				this.noTable.style.display = 'none';
			}
		}
		this.showCount = this.allRows.length;
		var base = this;
		for (var i=1;i<this.allRows.length;i++) {
			this.allRows[i].onclick = function() {
				if (!synchronizedClassImpl.getmutex() && this !== base.currentRow) {
					if (base.currentRow) {
						base.currentRow.removeAttribute('class');
					}
					this.className = 'selected';
					base.currentRow = this;
					docDataClass.setCurrentClient(this.getElementsByTagName('input')[0].value);
					if (!synchronizedClassImpl.getmutex()) {
						navigation.switchOnTab(1);
					}
				}
			}
		}
		this.displayTable();
	}	

	var DOMObjectsForTree = function() {
		this.root = document.getElementById('invoices').getElementsByClassName('tree')[0].getElementsByTagName('ul')[0];
		this.currentSelectedItem;
		this.remakeTree = function() {
			var rootParent = this.root.parentNode;
			rootParent.removeChild(this.root);
			this.root = document.createElement('ul');
			rootParent.insertBefore(this.root,rootParent.children[0]);
		}
		this.selectItem = function(number) {
			if (this.currentSelectedItem) {
				this.currentSelectedItem.removeAttribute('class');
			}
			this.currentSelectedItem=this.root.getElementsByClassName(number)[0].getElementsByTagName('a')[0];
			this.currentSelectedItem.className = 'selected';
		}
		this.buildTree = function() {
			this.remakeTree();
			if (!docDataClass.docTree['']) {
				for (var requestIndex in docDataClass.docTree) {
					var elemRequest = document.createElement("li");
					elemRequest.appendChild(document.createElement('a'));
					elemRequest.onclick = function() {
						if (!synchronizedClassImpl.getmutex()) {
							objectsForTree.selectItem(this.className);
							objectsForRequest.selectItem(this.className);
						}
					};
					this.root.appendChild(elemRequest);
					elemRequest.children[0].innerHTML = docDataClass.docTree[requestIndex].requestNumber;
					elemRequest.className = requestIndex;
					if (!docDataClass.docTree[requestIndex].insideRequests['']) {
						var rootInsideRequest = document.createElement('ul');
						elemRequest.appendChild(rootInsideRequest);
						for (var insideRequestIndex in docDataClass.docTree[requestIndex].insideRequests) {
							var elemInsideRequest = document.createElement("li");
							elemInsideRequest.innerHTML='<a>'+docDataClass.docTree[requestIndex].insideRequests[insideRequestIndex].insideRequestNumber+'</a>';
							rootInsideRequest.appendChild(elemInsideRequest);
							elemInsideRequest.onclick = function(event) {
								if (!synchronizedClassImpl.getmutex()) {
									objectsForRequest.selectItem(this.parentNode.parentNode.className);
									objectsForInsideRequest.selectItem(this.className);
									objectsForInsideRequestMain.selectItem(this.className);
									objectsForTree.selectItem(this.className);
								}
								event.cancelBubble = true;
							};
							elemInsideRequest.className = insideRequestIndex;
							if (!docDataClass.docTree[requestIndex].insideRequests[insideRequestIndex].invoices['']) {
								var rootInvoice = document.createElement('ul');
								elemInsideRequest.appendChild(rootInvoice);
								for (var invoice in docDataClass.docTree[requestIndex].insideRequests[insideRequestIndex].invoices) {
									var elemInvoice = document.createElement("li");
									elemInvoice.innerHTML='<a>'+docDataClass.docTree[requestIndex].insideRequests[insideRequestIndex].invoices[invoice].invoiceNumber+'</a>';
									rootInvoice.appendChild(elemInvoice);
									elemInvoice.className = invoice;
									elemInvoice.onclick = function(event) {
										if (!synchronizedClassImpl.getmutex()) {
											objectsForRequest.selectItem(this.parentNode.parentNode.parentNode.parentNode.className);
											objectsForInsideRequest.selectItem(this.parentNode.parentNode.className);
											objectsForInsideRequestMain.selectItem(this.parentNode.parentNode.className);
											objectsInvoiceMain.selectItem(this.className);
											objectsInvoice.selectItem(this.className);
											objectsForTree.selectItem(this.className);
										}
										event.cancelBubble = true;
									};
								}
							}
						}
					}
				}
				this.displayLists(1);
			}
			else {
				this.displayLists(0);
			}
		}
		this.displayLists = function(level) {
			for (var i=0; i<document.getElementsByClassName('noDocument').length;i++) {
				if (i<level*2) {
					document.getElementsByClassName('noDocument')[i].style.display='none';
					document.getElementsByClassName('documentData')[i].removeAttribute('style');
				} else {
					document.getElementsByClassName('documentData')[i].style.display='none';
					document.getElementsByClassName('noDocument')[i].removeAttribute('style');
				}
			}
		};
		this.displayLists(0);
	}

	var DOMObjectsForRequest = function() {
		this.root = document.getElementById('requestList').getElementsByClassName('tree')[0].getElementsByTagName('ul')[0];
		this.currentSelectedRequest;
		this.remakeList = function() {
			var rootParent = this.root.parentNode;
			rootParent.removeChild(this.root);
			this.root = document.createElement('ul');
			rootParent.insertBefore(this.root,rootParent.children[0]);
		}
		this.selectItem = function(number) {
			if (this.currentSelectedRequest) {
				this.currentSelectedRequest.removeAttribute('class');
			}
			this.currentSelectedRequest=this.root.getElementsByClassName(number)[0].getElementsByTagName('a')[0];
			this.currentSelectedRequest.className = 'selected';
			docDataClass.setCurrentRequest(number);
			if (!synchronizedClassImpl.getmutex()) {
				navigation.switchOnTab(2);
				navigation.goToTab(1);
			}
			objectsForInsideRequestMain.buildList();
			objectsForInsideRequest.buildList();
		}
		this.buildList = function() {
			this.remakeList();
			if (!docDataClass.docTree['']) {
				for (var requestIndex in docDataClass.docTree) {
					var elemRequest = document.createElement("li");
					elemRequest.appendChild(document.createElement('a'));
					elemRequest.onclick = function() {
						if (!synchronizedClassImpl.getmutex()) {
							objectsForRequest.selectItem(this.className);
							objectsForTree.selectItem(this.className);
						}
					};
					this.root.appendChild(elemRequest);
					elemRequest.className = requestIndex;
					elemRequest.children[0].innerHTML = docDataClass.docTree[requestIndex].requestNumber;
				}
			}
		}
	}
	
	var DOMObjectsForInsideRequest = function(insideRequestRoot) {
		this.root = document.getElementById(insideRequestRoot).getElementsByClassName('tree')[0].getElementsByTagName('ul')[0];
		this.currentSelectedInsideRequest;
		this.remakeList = function() {
			var rootParent = this.root.parentNode;
			rootParent.removeChild(this.root);
			this.root = document.createElement('ul');
			rootParent.insertBefore(this.root,rootParent.children[0]);
		}
		this.selectItem = function(number) {
			if (this.currentSelectedInsideRequest) {
				this.currentSelectedInsideRequest.removeAttribute('class');
			}
			this.currentSelectedInsideRequest=this.root.getElementsByClassName(number)[0].getElementsByTagName('a')[0];
			this.currentSelectedInsideRequest.className = 'selected';
			if (this===objectsForInsideRequestMain) {
				docDataClass.setCurrentInsideRequest(number);
				if (!synchronizedClassImpl.getmutex()) {
					navigation.switchOnTab(3);
					navigation.goToTab(2);
				}
				objectsInvoice.buildList();
				objectsInvoiceMain.buildList();
			}
		}
		this.buildList = function() {
			this.remakeList();
			if (!docDataClass.docTree[docDataClass.currentRequestObj.currentRequest].insideRequests['']) {
				for (var insideRequestIndex in docDataClass.docTree[docDataClass.currentRequestObj.currentRequest].insideRequests) {
					var elemInsideRequest = document.createElement("li");
					elemInsideRequest.appendChild(document.createElement('a'));
					elemInsideRequest.className = insideRequestIndex;
					elemInsideRequest.onclick = function() {
						if (!synchronizedClassImpl.getmutex()) {
							objectsForInsideRequest.selectItem(this.className);
							objectsForInsideRequestMain.selectItem(this.className);
							objectsForTree.selectItem(this.className);
						}
					};
					elemInsideRequest.children[0].innerHTML = docDataClass.docTree[docDataClass.currentRequestObj.currentRequest].insideRequests[insideRequestIndex].insideRequestNumber;
					this.root.appendChild(elemInsideRequest);
				}
			}
			objectsForTree.displayLists(2);
		}
		this.button = document.getElementById(insideRequestRoot).getElementsByTagName('button')[0];
		this.button.onclick = function() {
			objectForClientsTable.currentRow.className='mine';
			
			//////////
			
			event = event || window.event;
			if (event.preventDefault) {
				event.preventDefault();
			} else {
				event.returnValue = false;
			}
		}
	}

	var DOMObjectsForInvoice = function(invoiceRoot) {
		this.root = document.getElementById(invoiceRoot).getElementsByClassName('tree')[0].getElementsByTagName('ul')[0];
		this.currentSelectedInvoice;
		this.remakeList = function() {
			var rootParent = this.root.parentNode;
			rootParent.removeChild(this.root);
			this.root = document.createElement('ul');
			rootParent.insertBefore(this.root,rootParent.children[0]);
		}
		this.selectItem = function(number) {
			if (this.currentSelectedInvoice) {
				this.currentSelectedInvoice.removeAttribute('class');
			}
			this.currentSelectedInvoice=this.root.getElementsByClassName(number)[0].getElementsByTagName('a')[0];
			this.currentSelectedInvoice.className = 'selected';
			if (this===objectsInvoiceMain) {
				docDataClass.setCurrentInvoice(number);
				if (!synchronizedClassImpl.getmutex()) {
					navigation.switchOnTab(3);
					navigation.goToTab(3);
				}
			}
		}
		this.buildList = function() {
			this.remakeList();
			if (!docDataClass.docTree[docDataClass.currentRequestObj.currentRequest].insideRequests[docDataClass.currentInsideRequestObj.currentInsideRequest].invoices['']) {
				for (var invoiceIndex in docDataClass.docTree[docDataClass.currentRequestObj.currentRequest].insideRequests[docDataClass.currentInsideRequestObj.currentInsideRequest].invoices) {
					var elemInvoice = document.createElement("li");
					elemInvoice.appendChild(document.createElement('a'));
					elemInvoice.className = invoiceIndex;
					elemInvoice.onclick = function() {
						if (!synchronizedClassImpl.getmutex()) {
							objectsInvoice.selectItem(this.className);
							objectsInvoiceMain.selectItem(this.className);
							objectsForTree.selectItem(this.className);
						}
					};
					elemInvoice.children[0].innerHTML = docDataClass.docTree[docDataClass.currentRequestObj.currentRequest].insideRequests[docDataClass.currentInsideRequestObj.currentInsideRequest].invoices[invoiceIndex].invoiceNumber;
					this.root.appendChild(elemInvoice);
				}
			}
			objectsForTree.displayLists(3);
		}
		this.button = document.getElementById(invoiceRoot).getElementsByTagName('button')[0];
		this.button.onclick = function() {
			objectForClientsTable.currentRow.className='mine';
			
			//////////
			
			event = event || window.event;
			if (event.preventDefault) {
				event.preventDefault();
			} else {
				event.returnValue = false;
			}
		}
	}
	//function block
	function cancelClick(event) {
		if (event.preventDefault) {
			event.preventDefault();
		} else {
			event.returnValue = false;
		}		
	}
	
	function initialization() {
		navigation = new DOMObjectsForNavigation();
		objectForClientsTable = new DOMObjectsForClientsTable();
		docDataClass = new DocDataClass();
		objectsForTree = new DOMObjectsForTree();
		objectsForRequest = new DOMObjectsForRequest();
		objectsForInsideRequest = new DOMObjectsForInsideRequest('insideRequestListMain');
		objectsForInsideRequestMain = new DOMObjectsForInsideRequest('insideRequestList');
		objectsInvoice = new DOMObjectsForInvoice('invoiceListMain');
		objectsInvoiceMain = new DOMObjectsForInvoice('invoiceList');
		requestButtons = new DocButton(1);
		insideRequestButtons = new DocButton(2);
		invoiceButtons = new DocButton(3);
		for (var i=0;i<3;i++) {
			docInformation[i] = new DocInformation(i+1);
		}
	}
	//main block
	window.onload = function () {												// main method working after loading
		initialization();
	}
	Protoplasm.use('datepicker').transform('input.datepicker', {'timePicker' : 'true' , 'use24hrs' : 'true', 'locale':'ru_RU'});
</script>
</head>
<body>
	<div id="container">
		<div id="top-page">
<?php
	echo "\n";
	require_once "../../common_files/top-page.php";
?>
		</div>
<?php
	include_once "../../common_files/functions.php";
	if (!isset($_COOKIE["UserTypeID"])||!isset($_COOKIE["UserID"])||!md5IsEqual()) {
		foreach($_COOKIE as $key => $value) {
			setcookie($key,null,null,"/");
		}
		include_once "../../common_files/authorize.php";
	}
	elseif ($_COOKIE["UserTypeID"]==="0003"){
		include_once "content.php";
	}
	else {
		$number = 2;
		include_once "../../common_files/no_rights.php";
	}
?>
		<div id="foot-page"><?php require_once '../../common_files/foot-page.php'; ?></div>
	</div>
</body>
</html>