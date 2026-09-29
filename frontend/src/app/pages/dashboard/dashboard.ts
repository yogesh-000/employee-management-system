import { Component, inject } from '@angular/core';
import { Auth } from '../../core/auth';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
})
export class Dashboard {
  auth = inject(Auth);
  username = localStorage.getItem('username');
  role = this.auth.getRole();
}