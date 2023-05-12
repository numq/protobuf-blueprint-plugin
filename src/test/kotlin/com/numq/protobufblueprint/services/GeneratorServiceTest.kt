package com.numq.protobufblueprint.services

import com.intellij.openapi.components.service
import com.intellij.testFramework.LightPlatformTestCase

class GeneratorServiceTest : LightPlatformTestCase() {
    fun testGeneration() {
        val service = project.service<GeneratorService>()
        val input = """
            enum token access refresh self_signed
            message token userData
            service user getById create delete
        """.trimIndent()
        val expectedOutput = """
            enum Token {
              ACCESS = 0;
              REFRESH = 1;
              SELF_SIGNED = 2;
            }
            
            message Token {
              
            }
            
            message UserData {
              
            }
            
            message GetByIdRequest {
              
            }
            
            message GetByIdResponse {
              
            }
            
            message CreateRequest {
              
            }
            
            message CreateResponse {
              
            }
            
            message DeleteRequest {
              
            }
            
            message DeleteResponse {
              
            }
            
            service UserService {
              rpc GetById(GetByIdRequest) returns (GetByIdResponse);
              rpc Create(CreateRequest) returns (CreateResponse);
              rpc Delete(DeleteRequest) returns (DeleteResponse);
            }
        """.trimIndent()
        val output = service.generate(input)
        assertEquals(expectedOutput, output)
    }
}