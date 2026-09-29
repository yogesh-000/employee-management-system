import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  template: `<h2>Welcome, {{ username }}</h2>
             <p>You are logged in as <strong>{{ role }}</strong>.</p>`,
})
export class Home {
  username = localStorage.getItem('username');
  role = localStorage.getItem('role');
}