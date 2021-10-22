import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../login/login.service';
import { UserManagementService } from '../../admin/user-management/service/user-management.service';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  authorities: string[] = [];
  isNavbarCollapsed = false;

  constructor(private loginService: LoginService, private userService: UserManagementService) {}

  ngOnInit(): void {
    this.userService.authorities().subscribe(res => {
      this.authorities = res;
    });
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
