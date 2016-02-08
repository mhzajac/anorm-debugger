package anorm

case class ResultRow(metaData: MetaData, data: List[Any]) extends Row

object Results {

	val barMeta = MetaData(
		List(
			MetaDataItem(ColumnName("bars.id", Some("id")), false, "java.lang.Integer"),
			MetaDataItem(ColumnName("bars.baz", Some("baz")), false, "java.lang.Boolean"),
			MetaDataItem(ColumnName("bars.buzz", Some("buzz")), true, "java.lang.Integer")
		)
	)

	val singleBarRow = ResultRow(barMeta, List(1L, true, null))

	val listBarMeta = Stream(
		ResultRow(barMeta, List(1L, true, null)),
		ResultRow(barMeta, List(2L, true, "a")),
		ResultRow(barMeta, List(3L, false, "b"))
	)

}
