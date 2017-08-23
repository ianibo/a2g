package com.k_int.a2g.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.k_int.a2g.antlr.*;

import org.apache.log4j.*
import groovy.util.logging.*



/**
 * This class provides an empty implementation of {@link ASNListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
@Log4j2
public class A2GListener extends ASNBaseListener {

  private java.util.Map a2g_module_repository;
  private typemap = [:]
  private current_assignment = null;

  private String extractIdentifier(List<TerminalNode> tnl) {
    // log.debug("extractIdentifier:${tnl.class.name} ${tnl.join('-')}");
    // tnl.each { tn ->
    //   log.debug("Extract terminal node : ${tn.class.name} ${tn}");
    // }
    tnl.collect { tn -> tn.getText() }.join(' ')
  }

  private Object getSingleValue(Object v) {
    Object result = null;
    if ( v instanceof List ) {
      if ( v.size() == 1 ) {
        result=v.get(0);
      }
      else {
        throw new RuntimeException("Request to getSingleValue for ${v} but cardinality is not 1");
      }
    }
    else {
      result = v
    }
    return v
  }

  public A2GListener() {
    super();
  }

  public A2GListener(java.util.Map a2g_module_repository) {
    super();
    log.debug("new A2GListener with access to a2g_module_repository");
    this.a2g_module_repository = a2g_module_repository;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterModuleDefinition(ASNParser.ModuleDefinitionContext ctx) { 
    log.debug("enterModuleDefinition ${ctx.IDENTIFIER()}");
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitModuleDefinition(ASNParser.ModuleDefinitionContext ctx) { 
    // log.debug("lexitModuleDefinition ${ctx.IDENTIFIER()}");
    // log.debug("lexitModuleDefinition -- assignmentList: ${ctx.moduleBody()?.assignmentList()}");

    if ( a2g_module_repository != null ) {
      String definition_identifier = extractIdentifier(ctx.IDENTIFIER());
      if ( a2g_module_repository[definition_identifier] == null ) {
        log.debug("Parsed ASN.1, registering new definitions for \"${definition_identifier}\" (${definition_identifier.class.name})- typemap contains ${typemap?.size()} entries");
        a2g_module_repository[definition_identifier] = [ typemap: typemap ];
      }
      else {
        throw new RuntimeException('Attempt to load second set of ASN.1 definitions for an already specified identifier: '+definition_identifier);
      }
    }
    else {
      log.warn("No a2g_module_repository defined - this is essentually a NOOP");
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterTagDefault(ASNParser.TagDefaultContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitTagDefault(ASNParser.TagDefaultContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionDefault(ASNParser.ExtensionDefaultContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionDefault(ASNParser.ExtensionDefaultContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterModuleBody(ASNParser.ModuleBodyContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitModuleBody(ASNParser.ModuleBodyContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExports(ASNParser.ExportsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExports(ASNParser.ExportsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSymbolsExported(ASNParser.SymbolsExportedContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSymbolsExported(ASNParser.SymbolsExportedContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterImports(ASNParser.ImportsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitImports(ASNParser.ImportsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSymbolsImported(ASNParser.SymbolsImportedContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSymbolsImported(ASNParser.SymbolsImportedContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSymbolsFromModuleList(ASNParser.SymbolsFromModuleListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSymbolsFromModuleList(ASNParser.SymbolsFromModuleListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSymbolsFromModule(ASNParser.SymbolsFromModuleContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSymbolsFromModule(ASNParser.SymbolsFromModuleContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterGlobalModuleReference(ASNParser.GlobalModuleReferenceContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitGlobalModuleReference(ASNParser.GlobalModuleReferenceContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAssignedIdentifier(ASNParser.AssignedIdentifierContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAssignedIdentifier(ASNParser.AssignedIdentifierContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSymbolList(ASNParser.SymbolListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSymbolList(ASNParser.SymbolListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSymbol(ASNParser.SymbolContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSymbol(ASNParser.SymbolContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAssignmentList(ASNParser.AssignmentListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAssignmentList(ASNParser.AssignmentListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAssignment(ASNParser.AssignmentContext ctx) { 
    log.debug("A2GListener::enterAssignment(${ctx.IDENTIFIER()})"); 
    this.current_assignment = [:]
     // Will be one of (  valueAssignment
     //    | typeAssignment
     //    | parameterizedAssignment
     //    | objectClassAssignment
     //   )

  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAssignment(ASNParser.AssignmentContext ctx) { 
    def type_identifier = ctx.IDENTIFIER().getText()
    log.debug(ctx);
    log.debug("EXIT ASSIGNMENT -> Adding definition for ${type_identifier} ${type_identifier.class.name} to this definitions typemap");
    this.typemap[type_identifier] = this.current_assignment
    this.current_assignment = null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSequenceType(ASNParser.SequenceTypeContext ctx) { 
    this.current_assignment.type='SEQUENCE';
    this.current_assignment.members = []
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSequenceType(ASNParser.SequenceTypeContext ctx) { 
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAndException(ASNParser.ExtensionAndExceptionContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAndException(ASNParser.ExtensionAndExceptionContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterOptionalExtensionMarker(ASNParser.OptionalExtensionMarkerContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitOptionalExtensionMarker(ASNParser.OptionalExtensionMarkerContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterComponentTypeLists(ASNParser.ComponentTypeListsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitComponentTypeLists(ASNParser.ComponentTypeListsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterRootComponentTypeList(ASNParser.RootComponentTypeListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitRootComponentTypeList(ASNParser.RootComponentTypeListContext ctx) { 
    // log.debug("A2GListener::exitRootComponentTypeList");
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterComponentTypeList(ASNParser.ComponentTypeListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitComponentTypeList(ASNParser.ComponentTypeListContext ctx) { }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterComponentType(ASNParser.ComponentTypeContext ctx) { 
    if ( ctx.namedType() ) {
      if ( this.current_assignment.members != null ) {
        def nt = ctx.namedType();
        if ( nt.type() ) {
          if ( nt.type().builtinType() ) {
            if ( nt.type().builtinType().octetStringType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'octetString'])
            }
            else if ( nt.type().builtinType().bitStringType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'bitString'])
            }
            else if ( nt.type().builtinType().choiceType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'choice'])
            }
            else if ( nt.type().builtinType().taggedType() ) {
              log.debug("componentType::tagged type");
              def tt = nt.type().builtinType().taggedType();

              def tc = null;
              if ( tt.tag().tagClass() ) {
                if ( tt.tag().tagClass().UNIVERSAL_LITERAL() ) tc = new Integer(0) // 'UNIVERSAL';
                if ( tt.tag().tagClass().APPLICATION_LITERAL() ) tc = new Integer(64) // 'APPLICATION';
                if ( tt.tag().tagClass().PRIVATE_LITERAL() ) tc = new Integer(192) // 'PRIVATE';
              }
              else {
                tc = new Integer(128); // CONTEXT SPECIFIC
              }

              // Tag mode defaults to module tag mode or explicit, leave to higher level impl to sort this out!
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), 
                                                   elementCat:'builtin', 
                                                   elementType:'tagged',
                                                   tag: [tag_class_number:Integer.parseInt(tt.tag().tagClassNumber().NUMBER().toString()), 
                                                         tag_class: tc],
                                                   type: tt.type(),  // tt.type().builtinType or tt.type().referencedType()
                                                   tagMode : tt.IMPLICIT_LITERAL() ? 'IMPLICIT' : ( tt.EXPLICIT_LITERAL() ? 'EXPLICIT' : null ) ])

            }
            else if ( nt.type().builtinType().enumeratedType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'enumerated'])
            }
            else if ( nt.type().builtinType().integerType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'integer'])
            }
            else if ( nt.type().builtinType().sequenceType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'sequence'])
            }
            else if ( nt.type().builtinType().sequenceOfType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'sequenceOf'])
            }
            else if ( nt.type().builtinType().setType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'set'])
            }
            else if ( nt.type().builtinType().setOfType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'setOf'])
            }
            else if ( nt.type().builtinType().objectidentifiertype() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'objectidentifier'])
            }
            else if ( nt.type().builtinType().objectClassFieldType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'objectClass'])
            }
            else if ( nt.type().builtinType().nullType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'nullType'])
            }
            else if ( nt.type().builtinType().booleanType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER(), elementCat:'builtin', elementType:'boolean'])
            }
            else {
              throw new RuntimeException("Unhandled builtin type");
            }

          }
          else if ( nt.type().referencedType() ) {
            if ( nt.type().referencedType().definedType() ) {
              this.current_assignment.members.add([elementName: nt.IDENTIFIER().getText(), 
                                                   elementCat:'defined', 
                                                   elementType:extractIdentifier(nt.type().referencedType().definedType().IDENTIFIER())])
            }
            else {
              throw new RuntimeException("Can't handle non-defined type for referenceType");
            }
          }
          else {
            throw new RuntimeException("enterComponentType - named type was not builtin or reference");
          }
        }
        else {
          throw new RuntimeException("enterComponentType namedType seems not to have a type ");
        }
      }
      else {
        throw new RuntimeException("Processing enterComponentType but this.current_assignment.members is null");
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitComponentType(ASNParser.ComponentTypeContext ctx) { 
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditions(ASNParser.ExtensionAdditionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditions(ASNParser.ExtensionAdditionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditionList(ASNParser.ExtensionAdditionListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditionList(ASNParser.ExtensionAdditionListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAddition(ASNParser.ExtensionAdditionContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAddition(ASNParser.ExtensionAdditionContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditionGroup(ASNParser.ExtensionAdditionGroupContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditionGroup(ASNParser.ExtensionAdditionGroupContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterVersionNumber(ASNParser.VersionNumberContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitVersionNumber(ASNParser.VersionNumberContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSequenceOfType(ASNParser.SequenceOfTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSequenceOfType(ASNParser.SequenceOfTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSizeConstraint(ASNParser.SizeConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSizeConstraint(ASNParser.SizeConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterParameterizedAssignment(ASNParser.ParameterizedAssignmentContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitParameterizedAssignment(ASNParser.ParameterizedAssignmentContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterParameterList(ASNParser.ParameterListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitParameterList(ASNParser.ParameterListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterParameter(ASNParser.ParameterContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitParameter(ASNParser.ParameterContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterParamGovernor(ASNParser.ParamGovernorContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitParamGovernor(ASNParser.ParamGovernorContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterGovernor(ASNParser.GovernorContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitGovernor(ASNParser.GovernorContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectClassAssignment(ASNParser.ObjectClassAssignmentContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectClassAssignment(ASNParser.ObjectClassAssignmentContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectClass(ASNParser.ObjectClassContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectClass(ASNParser.ObjectClassContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterDefinedObjectClass(ASNParser.DefinedObjectClassContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitDefinedObjectClass(ASNParser.DefinedObjectClassContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterUsefulObjectClassReference(ASNParser.UsefulObjectClassReferenceContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitUsefulObjectClassReference(ASNParser.UsefulObjectClassReferenceContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExternalObjectClassReference(ASNParser.ExternalObjectClassReferenceContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExternalObjectClassReference(ASNParser.ExternalObjectClassReferenceContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectClassDefn(ASNParser.ObjectClassDefnContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectClassDefn(ASNParser.ObjectClassDefnContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterWithSyntaxSpec(ASNParser.WithSyntaxSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitWithSyntaxSpec(ASNParser.WithSyntaxSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSyntaxList(ASNParser.SyntaxListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSyntaxList(ASNParser.SyntaxListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterTokenOrGroupSpec(ASNParser.TokenOrGroupSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitTokenOrGroupSpec(ASNParser.TokenOrGroupSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterOptionalGroup(ASNParser.OptionalGroupContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitOptionalGroup(ASNParser.OptionalGroupContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterRequiredToken(ASNParser.RequiredTokenContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitRequiredToken(ASNParser.RequiredTokenContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterLiteral(ASNParser.LiteralContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitLiteral(ASNParser.LiteralContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterPrimitiveFieldName(ASNParser.PrimitiveFieldNameContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitPrimitiveFieldName(ASNParser.PrimitiveFieldNameContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterFieldSpec(ASNParser.FieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitFieldSpec(ASNParser.FieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterTypeFieldSpec(ASNParser.TypeFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitTypeFieldSpec(ASNParser.TypeFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterTypeOptionalitySpec(ASNParser.TypeOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitTypeOptionalitySpec(ASNParser.TypeOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterFixedTypeValueFieldSpec(ASNParser.FixedTypeValueFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitFixedTypeValueFieldSpec(ASNParser.FixedTypeValueFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterValueOptionalitySpec(ASNParser.ValueOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitValueOptionalitySpec(ASNParser.ValueOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterVariableTypeValueFieldSpec(ASNParser.VariableTypeValueFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitVariableTypeValueFieldSpec(ASNParser.VariableTypeValueFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterFixedTypeValueSetFieldSpec(ASNParser.FixedTypeValueSetFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitFixedTypeValueSetFieldSpec(ASNParser.FixedTypeValueSetFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterValueSetOptionalitySpec(ASNParser.ValueSetOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitValueSetOptionalitySpec(ASNParser.ValueSetOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObject(ASNParser.ObjectContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObject(ASNParser.ObjectContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterParameterizedObject(ASNParser.ParameterizedObjectContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitParameterizedObject(ASNParser.ParameterizedObjectContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterDefinedObject(ASNParser.DefinedObjectContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitDefinedObject(ASNParser.DefinedObjectContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectSet(ASNParser.ObjectSetContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectSet(ASNParser.ObjectSetContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectSetSpec(ASNParser.ObjectSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectSetSpec(ASNParser.ObjectSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterFieldName(ASNParser.FieldNameContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitFieldName(ASNParser.FieldNameContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterValueSet(ASNParser.ValueSetContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitValueSet(ASNParser.ValueSetContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterElementSetSpecs(ASNParser.ElementSetSpecsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitElementSetSpecs(ASNParser.ElementSetSpecsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterRootElementSetSpec(ASNParser.RootElementSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitRootElementSetSpec(ASNParser.RootElementSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAdditionalElementSetSpec(ASNParser.AdditionalElementSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAdditionalElementSetSpec(ASNParser.AdditionalElementSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterElementSetSpec(ASNParser.ElementSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitElementSetSpec(ASNParser.ElementSetSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterUnions(ASNParser.UnionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitUnions(ASNParser.UnionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExclusions(ASNParser.ExclusionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExclusions(ASNParser.ExclusionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterIntersections(ASNParser.IntersectionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitIntersections(ASNParser.IntersectionsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterUnionMark(ASNParser.UnionMarkContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitUnionMark(ASNParser.UnionMarkContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterIntersectionMark(ASNParser.IntersectionMarkContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitIntersectionMark(ASNParser.IntersectionMarkContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterElements(ASNParser.ElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitElements(ASNParser.ElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectSetElements(ASNParser.ObjectSetElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectSetElements(ASNParser.ObjectSetElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterIntersectionElements(ASNParser.IntersectionElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitIntersectionElements(ASNParser.IntersectionElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSubtypeElements(ASNParser.SubtypeElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSubtypeElements(ASNParser.SubtypeElementsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterVariableTypeValueSetFieldSpec(ASNParser.VariableTypeValueSetFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitVariableTypeValueSetFieldSpec(ASNParser.VariableTypeValueSetFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectFieldSpec(ASNParser.ObjectFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectFieldSpec(ASNParser.ObjectFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectOptionalitySpec(ASNParser.ObjectOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectOptionalitySpec(ASNParser.ObjectOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectSetFieldSpec(ASNParser.ObjectSetFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectSetFieldSpec(ASNParser.ObjectSetFieldSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectSetOptionalitySpec(ASNParser.ObjectSetOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectSetOptionalitySpec(ASNParser.ObjectSetOptionalitySpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterTypeAssignment(ASNParser.TypeAssignmentContext ctx) { 
    log.debug("enterTypeAssignment");
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitTypeAssignment(ASNParser.TypeAssignmentContext ctx) { 
    log.debug("exitTypeAssignment");
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterValueAssignment(ASNParser.ValueAssignmentContext ctx) { 
    log.debug("enterValueAssignment");
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitValueAssignment(ASNParser.ValueAssignmentContext ctx) { 
    log.debug("A2GListener::exitValueAssignment");
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterType(ASNParser.TypeContext ctx) { }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitType(ASNParser.TypeContext ctx) { 
    log.debug("A2GListener::exitType");
    if ( ctx.builtinType() != null ) {
      log.debug("    -> BuiltinType");
      log.debug("      -> octetString:${ctx.builtinType().octetStringType()}");
    }

    if ( ctx.referencedType() != null ) {
      log.debug("    -> Referenced Type");
      log.debug("      -> ${ctx.referencedType().definedType()}");
    }

    log.debug("    -> ${ctx.constraint()}");
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterBuiltinType(ASNParser.BuiltinTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitBuiltinType(ASNParser.BuiltinTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectClassFieldType(ASNParser.ObjectClassFieldTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectClassFieldType(ASNParser.ObjectClassFieldTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSetType(ASNParser.SetTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSetType(ASNParser.SetTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSetOfType(ASNParser.SetOfTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSetOfType(ASNParser.SetOfTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterReferencedType(ASNParser.ReferencedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitReferencedType(ASNParser.ReferencedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterDefinedType(ASNParser.DefinedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitDefinedType(ASNParser.DefinedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterConstraint(ASNParser.ConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitConstraint(ASNParser.ConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterConstraintSpec(ASNParser.ConstraintSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitConstraintSpec(ASNParser.ConstraintSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterUserDefinedConstraint(ASNParser.UserDefinedConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitUserDefinedConstraint(ASNParser.UserDefinedConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterGeneralConstraint(ASNParser.GeneralConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitGeneralConstraint(ASNParser.GeneralConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterUserDefinedConstraintParameter(ASNParser.UserDefinedConstraintParameterContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitUserDefinedConstraintParameter(ASNParser.UserDefinedConstraintParameterContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterTableConstraint(ASNParser.TableConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitTableConstraint(ASNParser.TableConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSimpleTableConstraint(ASNParser.SimpleTableConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSimpleTableConstraint(ASNParser.SimpleTableConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterContentsConstraint(ASNParser.ContentsConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitContentsConstraint(ASNParser.ContentsConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSubtypeConstraint(ASNParser.SubtypeConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSubtypeConstraint(ASNParser.SubtypeConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterValue(ASNParser.ValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitValue(ASNParser.ValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterBuiltinValue(ASNParser.BuiltinValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitBuiltinValue(ASNParser.BuiltinValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectIdentifierValue(ASNParser.ObjectIdentifierValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectIdentifierValue(ASNParser.ObjectIdentifierValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjIdComponentsList(ASNParser.ObjIdComponentsListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjIdComponentsList(ASNParser.ObjIdComponentsListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjIdComponents(ASNParser.ObjIdComponentsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjIdComponents(ASNParser.ObjIdComponentsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterIntegerValue(ASNParser.IntegerValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitIntegerValue(ASNParser.IntegerValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterChoiceValue(ASNParser.ChoiceValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitChoiceValue(ASNParser.ChoiceValueContext ctx) { 
    // this.current_assignment.members.add(CHOICE)...
    log.debug("exitChoiceValue(${ctx})");
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterEnumeratedValue(ASNParser.EnumeratedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitEnumeratedValue(ASNParser.EnumeratedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSignedNumber(ASNParser.SignedNumberContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSignedNumber(ASNParser.SignedNumberContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterChoiceType(ASNParser.ChoiceTypeContext ctx) { 
    this.current_assignment.type='CHOICE';
    this.current_assignment.members = []
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitChoiceType(ASNParser.ChoiceTypeContext ctx) { 
    log.debug("A2GListener::exitChoiceType");
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAlternativeTypeLists(ASNParser.AlternativeTypeListsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAlternativeTypeLists(ASNParser.AlternativeTypeListsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditionAlternatives(ASNParser.ExtensionAdditionAlternativesContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditionAlternatives(ASNParser.ExtensionAdditionAlternativesContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditionAlternativesList(ASNParser.ExtensionAdditionAlternativesListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditionAlternativesList(ASNParser.ExtensionAdditionAlternativesListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditionAlternative(ASNParser.ExtensionAdditionAlternativeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditionAlternative(ASNParser.ExtensionAdditionAlternativeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExtensionAdditionAlternativesGroup(ASNParser.ExtensionAdditionAlternativesGroupContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExtensionAdditionAlternativesGroup(ASNParser.ExtensionAdditionAlternativesGroupContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterRootAlternativeTypeList(ASNParser.RootAlternativeTypeListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitRootAlternativeTypeList(ASNParser.RootAlternativeTypeListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAlternativeTypeList(ASNParser.AlternativeTypeListContext ctx) { 
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAlternativeTypeList(ASNParser.AlternativeTypeListContext ctx) { 

    // Register the options for a choice
    log.debug("exitAlternativeTypeList -- Registering possible options for CHOICE");
    ctx.namedType().each { named_type ->
      log.debug("CHOICE OPTION ${named_type.IDENTIFIER()}");
      def type = named_type.type()
      if ( type.builtinType() ) {
        def bit = type.builtinType()
        def builtin_type_info = extractBuiltinTypeInfo(bit);
        builtin_type_info.elementName = named_type.IDENTIFIER()
        this.current_assignment.members.add(builtin_type_info)
        log.debug("  --> is a builtin type with type ${builtin_type_info}");
      }
      else if ( type.referencedType() ) {
        def rt = type.referencedType()
        def dt = rt.definedType().IDENTIFIER()
        def rt_info = [elementName:named_type.IDENTIFIER(), elementCat:'defined', elementType:extractIdentifier(dt)];
        log.debug("  --> is a referenced type with type ${rt_info}");

        this.current_assignment.members.add(rt_info);
      }
      else {
        throw new RuntimeException("Unable to figure out what to do with named type processing choice");
      }
    }
  }

  public Map extractElementTypeInfo(tt) {

    Map result = [:]

    if ( tt.builtinType() ) {
      result = extractBuiltinTypeInfo(tt.builtinType() )
    }
    else if ( tt.referencedType() ) {
      result = [ elementCat:'defined', elementType: extractIdentifier(tt.referencedType().definedType().IDENTIFIER()) ]
    }
    else {
      throw new RuntimeException("Unhandled element type");
    }
    return result;
  }

  public Map extractBuiltinTypeInfo(bit) {

    log.debug("extractBuiltinTypeInfo");

    def result = [:]

    if ( bit.octetStringType() ) {
      result = [elementCat:'builtin', elementType:'octetString']
    }
    else if ( bit.bitStringType() ) {
      result = [elementCat:'builtin', elementType:'bitString']
    }
    else if ( bit.choiceType() ) {
      result = [elementCat:'builtin', elementType:'choice']
    }
    else if ( bit.taggedType() ) {

      log.debug("extractBuiltinTypeInfo componentType::tagged type");

      def tt = bit.taggedType();

      def tc = null;
      if ( tt.tag().tagClass() ) {
        if ( tt.tag().tagClass().UNIVERSAL_LITERAL() ) tc = new Integer(0) // 'UNIVERSAL';
        if ( tt.tag().tagClass().APPLICATION_LITERAL() ) tc = new Integer(64) // 'APPLICATION';
        if ( tt.tag().tagClass().PRIVATE_LITERAL() ) tc = new Integer(192) // 'PRIVATE';
      }
      else {
        tc = new Integer(128); // CONTEXT SPECIFIC
      }

      def type_info = extractElementTypeInfo(tt.type());

      // Tag mode defaults to module tag mode or explicit, leave to higher level impl to sort this out!
      result = [elementCat:'builtin',
                elementType:'tagged',
                tag: [tag_class_number: Integer.parseInt(tt.tag().tagClassNumber().NUMBER().toString()), tag_class: tc],
                type: type_info,
                tagMode : tt.IMPLICIT_LITERAL() ? 'IMPLICIT' : ( tt.EXPLICIT_LITERAL() ? 'EXPLICIT' : null ) ]
    }
    else if ( bit.enumeratedType() ) {
      result = [elementCat:'builtin', elementType:'enumerated']
    }
    else if ( bit.integerType() ) {
      result = [elementCat:'builtin', elementType:'integer']
    }
    else if ( bit.sequenceType() ) {
      result = [elementCat:'builtin', elementType:'sequence']
    }
    else if ( bit.sequenceOfType() ) {
      result = [elementCat:'builtin', elementType:'sequenceOf']
    }
    else if ( bit.setType() ) {
      result = [elementCat:'builtin', elementType:'set']
    }
    else if ( bit.setOfType() ) {
      result = [elementCat:'builtin', elementType:'setOf']
    }
    else if ( bit.objectidentifiertype() ) {
      result = [elementCat:'builtin', elementType:'objectidentifier']
    }
    else if ( bit.objectClassFieldType() ) {
      result = [elementCat:'builtin', elementType:'objectClass']
    }
    else if ( bit.nullType() ) {
      result = [elementCat:'builtin', elementType:'nullType']
    }
    else if ( bit.booleanType() ) {
      result = [elementCat:'builtin', elementType:'boolean']
    }
    else {
      throw new RuntimeException("Unhandled builtin type");
    }

    return result
  }


  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterNamedType(ASNParser.NamedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitNamedType(ASNParser.NamedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterEnumeratedType(ASNParser.EnumeratedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitEnumeratedType(ASNParser.EnumeratedTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterEnumerations(ASNParser.EnumerationsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitEnumerations(ASNParser.EnumerationsContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterRootEnumeration(ASNParser.RootEnumerationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitRootEnumeration(ASNParser.RootEnumerationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterEnumeration(ASNParser.EnumerationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitEnumeration(ASNParser.EnumerationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterEnumerationItem(ASNParser.EnumerationItemContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitEnumerationItem(ASNParser.EnumerationItemContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterNamedNumber(ASNParser.NamedNumberContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitNamedNumber(ASNParser.NamedNumberContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterDefinedValue(ASNParser.DefinedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitDefinedValue(ASNParser.DefinedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterParameterizedValue(ASNParser.ParameterizedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitParameterizedValue(ASNParser.ParameterizedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterSimpleDefinedValue(ASNParser.SimpleDefinedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitSimpleDefinedValue(ASNParser.SimpleDefinedValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterActualParameterList(ASNParser.ActualParameterListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitActualParameterList(ASNParser.ActualParameterListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterActualParameter(ASNParser.ActualParameterContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitActualParameter(ASNParser.ActualParameterContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExceptionSpec(ASNParser.ExceptionSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExceptionSpec(ASNParser.ExceptionSpecContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterExceptionIdentification(ASNParser.ExceptionIdentificationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitExceptionIdentification(ASNParser.ExceptionIdentificationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAdditionalEnumeration(ASNParser.AdditionalEnumerationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAdditionalEnumeration(ASNParser.AdditionalEnumerationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterIntegerType(ASNParser.IntegerTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitIntegerType(ASNParser.IntegerTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterNamedNumberList(ASNParser.NamedNumberListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitNamedNumberList(ASNParser.NamedNumberListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterObjectidentifiertype(ASNParser.ObjectidentifiertypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitObjectidentifiertype(ASNParser.ObjectidentifiertypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterComponentRelationConstraint(ASNParser.ComponentRelationConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitComponentRelationConstraint(ASNParser.ComponentRelationConstraintContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterAtNotation(ASNParser.AtNotationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitAtNotation(ASNParser.AtNotationContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterLevel(ASNParser.LevelContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitLevel(ASNParser.LevelContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterComponentIdList(ASNParser.ComponentIdListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitComponentIdList(ASNParser.ComponentIdListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterOctetStringType(ASNParser.OctetStringTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitOctetStringType(ASNParser.OctetStringTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterBitStringType(ASNParser.BitStringTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitBitStringType(ASNParser.BitStringTypeContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterNamedBitList(ASNParser.NamedBitListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitNamedBitList(ASNParser.NamedBitListContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterNamedBit(ASNParser.NamedBitContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitNamedBit(ASNParser.NamedBitContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterBooleanValue(ASNParser.BooleanValueContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitBooleanValue(ASNParser.BooleanValueContext ctx) { }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void enterEveryRule(ParserRuleContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void exitEveryRule(ParserRuleContext ctx) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void visitTerminal(TerminalNode node) { }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   */
  @Override public void visitErrorNode(ErrorNode node) { }
}
