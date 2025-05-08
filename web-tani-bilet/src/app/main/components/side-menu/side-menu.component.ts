import { NgClass, CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink } from '@angular/router';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';

interface MenuItem {
  title: string;
  link: string;
  icon: string;
  items?: MenuItem[];
  tooltip?: string;
  hasSubItems?: boolean;
}

interface SubMenuItem {
  title: string;
  link?: string;
  subtitle?: boolean;
  separator?: boolean;
}

@Component({
  selector: 'app-side-menu',
  standalone: true,
  imports: [
    MatDividerModule,
    MatExpansionModule,
    MatIconModule,
    MatListModule,
    MatSidenavModule,
    RouterLink,
    MatMenuModule,
    NgClass,
    CommonModule
  ],
  templateUrl: './side-menu.component.html',
  styleUrl: './side-menu.component.scss'
})
export class SideMenuComponent {
  readonly sideMenuRouterLinks: MenuItem[] = [
    {
      link: 'muzyka',
      title: 'Muzyka',
      icon: 'music',
    },
    {
      link: 'teatr',
      title: 'Teatr',
      icon: 'theatre',
    },
    {
      link: 'sport',
      title: 'Sport',
      icon: 'sport',
    }
  ]

  readonly sideMenuPopupOptions: Record<string, { items: SubMenuItem[] }> = {
    ['muzyka']: {
      items: [
        {
          link: '',
          title: 'Wszystkie',
        },
        {
          link: '',
          title: 'Festiwale',
        },
      ]
    },
    ['teatr']: {
      items: [
        {
          link: '',
          title: 'Musicale',
        },
        {
          link: '',
          title: 'Komedia',
        },
      ]
    },
    ['sport']: {
      items: [
        {
          link: '',
          title: 'Siatkówka',
        },
        {
          link: '',
          title: 'Piłka nożna',
        },
        {
          link: '',
          title: 'Koszykówka',
        },
        {
          link: '',
          title: 'Tenis ziemny',
        },
      ]
    }
  }

}
