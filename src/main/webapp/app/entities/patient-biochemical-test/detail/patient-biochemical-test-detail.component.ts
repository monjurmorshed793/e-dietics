import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatientBiochemicalTest } from '../patient-biochemical-test.model';

@Component({
  selector: 'jhi-patient-biochemical-test-detail',
  templateUrl: './patient-biochemical-test-detail.component.html',
})
export class PatientBiochemicalTestDetailComponent implements OnInit {
  patientBiochemicalTest: IPatientBiochemicalTest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientBiochemicalTest }) => {
      this.patientBiochemicalTest = patientBiochemicalTest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
