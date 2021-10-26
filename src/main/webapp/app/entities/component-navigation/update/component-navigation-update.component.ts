import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComponentNavigation, ComponentNavigation } from '../component-navigation.model';
import { ComponentNavigationService } from '../service/component-navigation.service';

@Component({
  selector: 'jhi-component-navigation-update',
  templateUrl: './component-navigation-update.component.html',
})
export class ComponentNavigationUpdateComponent implements OnInit {
  isSaving = false;

  componentNavigationsSharedCollection: IComponentNavigation[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    icon: [],
    location: [null, [Validators.required]],
    roles: [],
    parent: [],
  });

  constructor(
    protected componentNavigationService: ComponentNavigationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ componentNavigation }) => {
      this.updateForm(componentNavigation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const componentNavigation = this.createFromForm();
    if (componentNavigation.id !== undefined) {
      this.subscribeToSaveResponse(this.componentNavigationService.update(componentNavigation));
    } else {
      this.subscribeToSaveResponse(this.componentNavigationService.create(componentNavigation));
    }
  }

  trackComponentNavigationById(index: number, item: IComponentNavigation): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComponentNavigation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(componentNavigation: IComponentNavigation): void {
    this.editForm.patchValue({
      id: componentNavigation.id,
      name: componentNavigation.name,
      icon: componentNavigation.icon,
      location: componentNavigation.location,
      roles: componentNavigation.roles,
      parent: componentNavigation.parent,
    });

    this.componentNavigationsSharedCollection = this.componentNavigationService.addComponentNavigationToCollectionIfMissing(
      this.componentNavigationsSharedCollection,
      componentNavigation.parent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.componentNavigationService
      .query()
      .pipe(map((res: HttpResponse<IComponentNavigation[]>) => res.body ?? []))
      .pipe(
        map((componentNavigations: IComponentNavigation[]) =>
          this.componentNavigationService.addComponentNavigationToCollectionIfMissing(
            componentNavigations,
            this.editForm.get('parent')!.value
          )
        )
      )
      .subscribe((componentNavigations: IComponentNavigation[]) => (this.componentNavigationsSharedCollection = componentNavigations));
  }

  protected createFromForm(): IComponentNavigation {
    return {
      ...new ComponentNavigation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      location: this.editForm.get(['location'])!.value,
      roles: this.editForm.get(['roles'])!.value,
      parent: this.editForm.get(['parent'])!.value,
    };
  }
}
