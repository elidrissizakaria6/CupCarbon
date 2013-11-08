/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package consumer;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public abstract class AConsumption implements _Consommation, Cloneable {
	
	protected double consomUnit = 1 ;
	
	public AConsumption() {}
	

	/**
	 * Return the consumption unit
	 */
	public abstract double getConsumedUnit() ;
	
	/**
	 * Return the consumption unit x n
	 */
	public abstract double getConsumedUnit(double n) ;
	
	/**
	 * Clone AConsumption 
	 */
	
	@Override
	public AConsumption clone() throws CloneNotSupportedException {
		return (AConsumption) super.clone() ;
	}
	
}
