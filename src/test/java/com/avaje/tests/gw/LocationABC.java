package com.avaje.tests.gw;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.annotation.CreatedTimestamp;

/**
 * Order entity bean.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "o_location")
@CacheStrategy(useBeanCache = true)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class LocationABC implements Serializable {

	private static final long serialVersionUID = 1L;

	public LocationABC() {

	}

	public LocationABC(final String locationId) {
		this.locationId = locationId;
	}

	@Id
	Integer id;

	// String dtype;

	String locationId;

	// The owning location.
	@Column(nullable = false)
	@ManyToOne(optional = true)
	protected LocationABC parent;

	// The child locations.
	@OneToMany(mappedBy = "parent")
	@MapKey(name = "locationId")
	protected Map<String, LocationABC> locations = new HashMap<String, LocationABC>();

	@CreatedTimestamp
	Timestamp cretime;

	@Version
	Timestamp updtime;

	public String toString() {
		return id + " " + locationId;
	}

	/**
	 * Return id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set id.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public LocationABC getParent() {
		return parent;
	}

	public void setParent(final LocationABC parent) {
		this.parent = parent;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * Return cretime.
	 */
	public Timestamp getCretime() {
		return cretime;
	}

	/**
	 * Set cretime.
	 */
	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}

	/**
	 * Return updtime.
	 */
	public Timestamp getUpdtime() {
		return updtime;
	}

	/**
	 * Set updtime.
	 */
	public void setUpdtime(Timestamp updtime) {
		this.updtime = updtime;
	}

	/**
	 * Return locations.
	 */
	public List<LocationABC> getChildren() {
		// Cause the getter intercept to fire on locations.
		Map<String, LocationABC> theLocations = getLocations();
		return new ArrayList<LocationABC>(theLocations.values());
	}

	public Map<String, LocationABC> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, LocationABC> locations) {
		this.locations = locations;
	}

	public void addChildLocation(LocationABC location) {
		if (locations == null) {
			locations = new HashMap<String, LocationABC>();
		}
		locations.put(location.locationId, location);
	}
}
