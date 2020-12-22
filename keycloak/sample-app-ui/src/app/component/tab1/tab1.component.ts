import {Component, OnInit} from '@angular/core';
import {RemoteDataService} from '../../service/remote-data.service';
import {Category} from '../../model/Category';

@Component({
  selector: 'app-tab1',
  templateUrl: './tab1.component.html',
  styleUrls: ['./tab1.component.css']
})
export class Tab1Component implements OnInit {

  catories: Category[];

  constructor(private remoteDataService: RemoteDataService) {
  }

  ngOnInit(): void {
    this.remoteDataService.getMenu().subscribe(response => {
      this.catories = response;
      console.log(this.catories);
    });
  }

}
