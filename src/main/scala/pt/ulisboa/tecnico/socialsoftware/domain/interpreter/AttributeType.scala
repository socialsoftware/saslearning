package pt.ulisboa.tecnico.socialsoftware.domain.interpreter

/**
  * AttributeType is used as per Property methodology of Adaptive Object Model. As such,
  * the attributes of a class should be held as a collection of properties instead of
  * single instance variables.
  *
  * @param name the name of attribute type
  * @param typeOf the class of the type of the attribute
  * @tparam T the type of the attribute
  */
case class AttributeType[T](name: String, typeOf: Class[T])

/**
  * Represents an instance of the attribute type.
  *
  * @param name the name of the instance
  * @param specification the type of the attribute
  * @param value the concrete value of the attribute
  * @tparam T the type of the attribute
  */
case class Attribute[T](name: String, specification: AttributeType[T], value: T)
