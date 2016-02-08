package anorm

case class ResultRow(metaData: MetaData, data: List[Any]) extends Row

object Results {

	val metaData = ResultRow(
		MetaData(
			List(
				MetaDataItem(ColumnName("bars.id", Some("id")), false, "java.lang.Integer"),
				MetaDataItem(ColumnName("bars.baz", Some("baz")), false, "java.lang.Boolean"),
				MetaDataItem(ColumnName("bars.buzz", Some("buzz")), true, "java.lang.Integer")
			)
		),
		List(1L, true, null)
	)

}
