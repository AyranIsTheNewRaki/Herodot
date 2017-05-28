import { Category } from '../objects/category';

export const CATEGORIES: Category[] = [
    { displayName: "Painting", systemName: "painting", image: "painting.jpg", topCategory: "Movable" },
    { displayName: "Sculpture", systemName: "sculpture", image: "sculpture.jpg", topCategory: "Movable" },
    { displayName: "Books/Manuscript", systemName: "book", image: "book.jpg", topCategory: "Movable" },
    { displayName: "Handcraft", systemName: "handcraft", image: "handcraft.jpg", topCategory: "Movable" },
    { displayName: "Archeological Artifact", systemName: "archeologicalartifact", image: "archeologicalArtifact.jpg", topCategory: "Movable" },
    { displayName: "Archaeological Site", systemName: "archaeologicalsite", image: "archeologicalsite.jpg", topCategory: "Immovable" },
    { displayName: "Architecture", systemName: "architecture", image: "architecture.jpg", topCategory: "Immovable" },
    { displayName: "Museum", systemName: "museum", image: "museum.jpg", topCategory: "Immovable" },
    { displayName: "Natural Site", systemName: "naturalsite", image: "naturalsite.jpg", topCategory: "Immovable" },
    { displayName: "Underwater Site", systemName: "underwatersite", image: "underwatersite.jpg", topCategory: "Immovable" },
    { displayName: "Oral Tradition", systemName: "oraltradition", image: "OralTradition.jpg", topCategory: "Intangible" },
    { displayName: "Performing Art", systemName: "performingart", image: "performingart.jpg", topCategory: "Intangible" },
    { displayName: "Ritual/Tradition", systemName: "ritual", image: "ritual.jpg", topCategory: "Intangible" },
    { displayName: "Festival/Event", systemName: "festival", image: "festival.jpg", topCategory: "Intangible" },
    { displayName: "Other", systemName: "other", image: "other.jpg", topCategory: "Intangible" }
];