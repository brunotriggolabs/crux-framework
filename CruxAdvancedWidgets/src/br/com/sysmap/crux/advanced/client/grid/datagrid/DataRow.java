package br.com.sysmap.crux.advanced.client.grid.datagrid;

import br.com.sysmap.crux.advanced.client.grid.model.AbstractGrid;
import br.com.sysmap.crux.advanced.client.grid.model.Cell;
import br.com.sysmap.crux.advanced.client.grid.model.Row;
import br.com.sysmap.crux.core.client.datasource.EditableDataSourceRecord;

import com.google.gwt.dom.client.Element;

public class DataRow extends Row
{
	private EditableDataSourceRecord dataSourceRecord;
	
	protected DataRow(int index, Element elem, AbstractGrid<?, ?> grid, boolean hasSelectionCell)
	{
		super(index, elem, grid, hasSelectionCell);
	}

	/**
	 * @return the dataSourceRowId
	 */
	public EditableDataSourceRecord getDataSourceRecord()
	{
		return dataSourceRecord;
	}

	/**
	 * @param dataSourceRowId the dataSourceRowId to set
	 */
	void setDataSourceRecord(EditableDataSourceRecord dataSourceRowId)
	{
		this.dataSourceRecord = dataSourceRowId;
	}
	
	public Object getValue(String column)
	{
		int index = ((PagedDataGrid) getGrid()).getDataSource().getMetadata().getColumnPosition(column);
		return dataSourceRecord.get(index);
	}
	
	@Override
	protected void setCell(Cell cell, String column)
	{
		super.setCell(cell, column);
	}
	
	@Override
	protected void markAsSelected(boolean selected)
	{
		super.markAsSelected(selected);
	}
}