function isEmpty(DOMobj, isRedLight) {
	if (DOMobj.value==='') {
		if(isRedLight) {
			DOMobj.className='errorInput';
		}
		alert('Поле \''+DOMobj.previousSibling.childNodes[0].nodeValue.substr(0,DOMobj.previousSibling.childNodes[0].nodeValue.length-1).toLowerCase()+'\' не заполнено!');
		return true;
	}
	return false;
}
function isEqual(obj1,obj2,isRedLight) {
	if (obj1.value!=obj2.value) {
		if(isRedLight) {
			obj1.className='errorInput';
			obj2.className='errorInput';
		}
		alert('Пароли не совпадают!');
		return false;
	}
	return true;
}
function setFocusFor(src, condition) {
	for (i=0; i<src.length;i++) {
		elem = src[i];
		
		if (elem && elem.type===condition) {
			elem.onfocus=function(event) {
				this.removeAttribute('class');
			}
		}
	}
}
function isOneOf(objArr) {
	for (var i=0;i<objArr.length;i++) {
		if (objArr[i].value!=='')
			return true;
	}
	for (var i=0;i<objArr.length;i++) {
		objArr[i].className='errorInput';
	}
	alert('Должно быть заполнено хотя бы одно из выделенных полей!');
	return false;
}
function cancelMainButtonEvent(event) {
	event = event || window.event;
	if (event.preventDefault) {
		event.preventDefault();
	} else {
		event.returnValue = false;
	}
}
function dateFormatYYYYMMDD(date) {
	return date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2);
}
function dateFormatDDMMYYYY(date) {
	return ('0' + date.getDate()).slice(-2) + '.' + ('0' + (date.getMonth() + 1)).slice(-2) + '.' + date.getFullYear();
}
function getTime(date) {
	return ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2) + ':' +('0' + date.getSeconds()).slice(-2);
}