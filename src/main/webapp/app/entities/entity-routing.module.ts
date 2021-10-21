import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'food-category',
        data: { pageTitle: 'FoodCategories' },
        loadChildren: () => import('./food-category/food-category.module').then(m => m.FoodCategoryModule),
      },
      {
        path: 'measurement-type',
        data: { pageTitle: 'MeasurementTypes' },
        loadChildren: () => import('./measurement-type/measurement-type.module').then(m => m.MeasurementTypeModule),
      },
      {
        path: 'biochemical-test',
        data: { pageTitle: 'BiochemicalTests' },
        loadChildren: () => import('./biochemical-test/biochemical-test.module').then(m => m.BiochemicalTestModule),
      },
      {
        path: 'supplements',
        data: { pageTitle: 'Supplements' },
        loadChildren: () => import('./supplements/supplements.module').then(m => m.SupplementsModule),
      },
      {
        path: 'diet-nature',
        data: { pageTitle: 'DietNatures' },
        loadChildren: () => import('./diet-nature/diet-nature.module').then(m => m.DietNatureModule),
      },
      {
        path: 'nutrition-state',
        data: { pageTitle: 'NutritionStates' },
        loadChildren: () => import('./nutrition-state/nutrition-state.module').then(m => m.NutritionStateModule),
      },
      {
        path: 'activity-level',
        data: { pageTitle: 'ActivityLevels' },
        loadChildren: () => import('./activity-level/activity-level.module').then(m => m.ActivityLevelModule),
      },
      {
        path: 'patient',
        data: { pageTitle: 'Patients' },
        loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule),
      },
      {
        path: 'patient-biochemical-test',
        data: { pageTitle: 'PatientBiochemicalTests' },
        loadChildren: () => import('./patient-biochemical-test/patient-biochemical-test.module').then(m => m.PatientBiochemicalTestModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
