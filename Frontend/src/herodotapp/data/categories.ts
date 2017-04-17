import { Category } from '../objects/category';

export const CATEGORIES: Category[] = [
    { displayName: "Painting", systemName: "painting", image: "paintings.jpg", topCategory: "Movable" },
    { displayName: "Sculpture", systemName: "sculpture", image: "sculptures.jpg", topCategory: "Movable" },
    { displayName: "Books/Manuscript", systemName: "book", image: "books.jpg", topCategory: "Movable" },
    { displayName: "Handcraft", systemName: "handcraft", image: "books.jpg", topCategory: "Movable" },
    { displayName: "Archeological Artifact", systemName: "archeologicalartifact", image: "archaeologicalsites.jpg", topCategory: "Movable" },
    { displayName: "Archaeological Site", systemName: "archaeologicalsite", image: "archaeologicalsites.jpg", topCategory: "Immovable" },
    { displayName: "Architecture", systemName: "architecture", image: "archaeologicalsites.jpg", topCategory: "Immovable" },
    { displayName: "Museum", systemName: "museum", image: "museums.jpg", topCategory: "Immovable" },
    { displayName: "Natural Site", systemName: "naturalsite", image: "museums.jpg", topCategory: "Immovable" },
    { displayName: "Underwater Site", systemName: "underwatersite", image: "archaeologicalsites.jpg", topCategory: "Immovable" },
    { displayName: "Oral Tradition", systemName: "oraltradition", image: "paintings.jpg", topCategory: "Intangible" },
    { displayName: "Performing Art", systemName: "performingart", image: "paintings.jpg", topCategory: "Intangible" },
    { displayName: "Ritual/Tradition", systemName: "ritual", image: "paintings.jpg", topCategory: "Intangible" },
    { displayName: "Festival/Event", systemName: "festival", image: "paintings.jpg", topCategory: "Intangible" },
    { displayName: "Test", systemName: "festival", image: "paintings.jpg", topCategory: "Intangible" }
];