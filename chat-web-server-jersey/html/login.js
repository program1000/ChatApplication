import Contacts from './contacts.js';
import sendData from './sender.js';
import session from './status.js';
import validate from './validate.js';
import ErrorBox from './error-box.js';

export default class Login {
	constructor() {
		this.mainContainer=document.getElementById('main-container');
		this.contentRow=document.getElementById('main-content');
		this.nameLabel=document.getElementById('name-lbl');
		this.errorBox = undefined;
		this.create();
		this.contacts = new Contacts();
	}
	
	create() {
		var loginDiv = document.createElement('div');
		loginDiv.classList.add('form-signin');
		loginDiv.id='login-content';

		var loginForm = document.createElement('form');
		loginForm.classList.add('text-center');
		loginDiv.appendChild(loginForm);

		var heading = document.createElement('h1');
		heading.classList.add('h3','mb-3','fw-normal');
		heading.appendChild( document.createTextNode('Login') );
		loginForm.appendChild(heading);
		loginForm.appendChild(this._createField('login-inp','Username','Name'));
		loginForm.appendChild(this._createField('login-pwd','Password','Password'));

		var loginBtn = document.createElement('button');
		loginBtn.classList.add('w-100','btn','btn-lg','btn-primary');
		loginBtn.id='login-btn';
		loginBtn.type='button';
		loginBtn.appendChild( document.createTextNode('Sign in') );
		loginForm.appendChild( loginBtn );
		
		var respDiv = document.createElement('div');
		respDiv.classList.add('form-floating','invisible');
		var respInput = document.createElement('textarea');
		respInput.classList.add('btn-outline-danger','error-text');
		respInput.id='login-srv';
		respInput.rows=3;
		respDiv.appendChild(respInput);
		loginForm.appendChild(respDiv);

		this.mainContainer.appendChild( loginDiv );
		
		var nameInput = document.getElementById('login-inp');
		var pwdInput = document.getElementById('login-pwd');
		var self=this;
		loginBtn.onclick=function(e) {
		  e.preventDefault();
		  let obj={'name':nameInput.value,
		           'pass':pwdInput.value}
  	       sendData( 'submit', obj, self.loginOk.bind(self),{'json':true,'param':{'div':loginDiv,'name':obj['name'],
		                                                     'errDiv':respDiv,'errInp':respInput},'error':self.errorBox});
		};
		
		var logoutBtn = document.getElementById('logout-btn');
		logoutBtn.onclick=function(e) {
			e.preventDefault();
			self.contacts.clear();
		    for( let key in session ) {
				session[key]='';
			}
			self.contentRow.classList.add('behind');
			new Login();
		};
		
		var loginSrv = document.getElementById('login-srv');
		loginSrv.readOnly=true;
		
		this.errorBox = new ErrorBox( respDiv, respInput );
	}
	
	_createField(id,name,placeholder) {
		var div = document.createElement('div');
		div.classList.add('form-floating');
		var input = document.createElement('input');
		input.classList.add('form-control');
		input.type=name.toLowerCase();
		input.id=id;
		input.name=name.toLowerCase();
		input.placeholder=placeholder;
		div.appendChild(input);
		var label = document.createElement('label');
		label.htmlFor=name.toLowerCase();
		label.appendChild( document.createTextNode(name) );
		div.appendChild(label);
		return div;
	}

	loginOk( sessionInfo, loginSrc ){
		sessionInfo = validate(sessionInfo, this.errorBox, {'test':'userId'});
		if ( sessionInfo == undefined ) {
			return;
		}
		this.mainContainer.removeChild(loginSrc['div']);
		this.contentRow.classList.remove('behind');
		session['session']=sessionInfo['sessionId'];
		session['user']=sessionInfo['userId'];
		session['name']=loginSrc['name'];
		this.nameLabel.textContent=session['name'].substring(0,10);
		this.contacts.addContacts(sessionInfo['contacts']);
		this.contacts.listenToServer();
	}

}
