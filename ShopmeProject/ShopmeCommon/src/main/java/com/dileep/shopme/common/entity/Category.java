package com.dileep.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Column(length = 64, nullable = false, unique = true)
	private String alias;
	@Column(length = 128, nullable = false)
	private String image;

	private boolean enabled;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();
	
	public Category() {
	}

	public Category(Long id) {
		this.id = id;
	}

	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());

		return copyCategory;
	}
	
	public static Category copyIdAndName(Long id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);

		return copyCategory;
	}
	
	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setImage(category.getImage());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size() > 0);

		return copyCategory;
	}
	
	public static Category copyFull(Category category,String name) {
		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(name);

		return copyCategory;
	}
	
	public Category(Long id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	@Transient
	public String getImagePath() {
		if(id == null || image == null) return "/images/img-thumbnail.png";
		return "/category-images/"+ this.id+ "/" + this.image;
	}
	
	@Transient
	private boolean hasChildren;

	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	
	
}
