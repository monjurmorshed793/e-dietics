export interface IComponentNavigation {
  id?: string;
  name?: string;
  location?: string;
  roles?: string | null;
  parent?: IComponentNavigation | null;
}

export class ComponentNavigation implements IComponentNavigation {
  constructor(
    public id?: string,
    public name?: string,
    public location?: string,
    public roles?: string | null,
    public parent?: IComponentNavigation | null
  ) {}
}

export function getComponentNavigationIdentifier(componentNavigation: IComponentNavigation): string | undefined {
  return componentNavigation.id;
}
