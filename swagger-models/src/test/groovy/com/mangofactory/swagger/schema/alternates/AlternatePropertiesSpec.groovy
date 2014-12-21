package com.mangofactory.swagger.schema.alternates
import com.mangofactory.swagger.mixins.ModelProviderSupport
import com.mangofactory.swagger.mixins.TypesForTestingSupport
import com.mangofactory.swagger.schema.ModelProvider
import com.mangofactory.servicemodel.Model
import spock.lang.Specification

import static com.mangofactory.swagger.schema.ModelContext.*

@Mixin([ModelProviderSupport, TypesForTestingSupport])
class AlternatePropertiesSpec extends Specification {
  def "Nested properties that have alternate types defined are rendered correctly" () {
    given:
      ModelProvider modelProvider = providerThatSubstitutesLocalDateWithString()
      Model model = modelProvider.modelFor(inputParam(typeWithAlternateProperty())).get()
    expect:
      model.getName() == "TypeWithAlternateProperty"
      model.getProperties().containsKey("localDate")
      def modelProperty = model.getProperties().get("localDate")
      modelProperty.getType().getAbsoluteType() == "string"
      modelProperty.getQualifiedType() == "java.lang.String"
      modelProperty.getItems() == null
  }

  def "ResponseEntity«Void» renders correctly when an alternate type is provided" () {
    given:
      ModelProvider modelProvider = providerThatSubstitutesResponseEntityOfVoid()
      Model model = modelProvider.modelFor(inputParam(typeWithResponseEntityOfVoid())).get()
    expect:
      model.getName() == "GenericType«ResponseEntity«Void»»"
      model.getProperties().containsKey("genericField")
      def modelProperty = model.getProperties().get("genericField")
      modelProperty.getType().dataType.reference == "Void"
      modelProperty.getQualifiedType() == "java.lang.Void"
      modelProperty.getItems() == null
  }
}