```java
package com.demo.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.app.model.Document;
import com.demo.app.repository.DocRepository;

@ExtendWith(MockitoExtension.class)
class DocServiceImplTest {

    @Mock
    private DocRepository docRepository;

    @InjectMocks
    private DocServiceImpl docServiceImpl;

    @Test
    @DisplayName("Given a valid user ID, when findAllDocs is called, then it should return the list of documents from the repository")
    void givenValidUserId_whenFindAllDocs_thenReturnListOfDocuments() {
        // Arrange
        Long userId = 1L;
        Document doc1 = new Document();
        Document doc2 = new Document();
        List<Document> expectedDocs = Arrays.asList(doc1, doc2);
        when(docRepository.findUserDocs(userId)).thenReturn(expectedDocs);

        // Act
        List<Document> actualDocs = docServiceImpl.findAllDocs(userId);

        // Assert
        assertNotNull(actualDocs);
        assertEquals(2, actualDocs.size());
        assertSame(expectedDocs, actualDocs);
        verify(docRepository, times(1)).findUserDocs(userId);
    }

    @Test
    @DisplayName("Given a user ID with no documents, when findAllDocs is called, then it should return an empty list")
    void givenUserIdWithNoDocuments_whenFindAllDocs_thenReturnEmptyList() {
        // Arrange
        Long userId = 2L;
        List<Document> expectedDocs = Collections.emptyList();
        when(docRepository.findUserDocs(userId)).thenReturn(expectedDocs);

        // Act
        List<Document> actualDocs = docServiceImpl.findAllDocs(userId);

        // Assert
        assertNotNull(actualDocs);
        assertTrue(actualDocs.isEmpty());
        verify(docRepository, times(1)).findUserDocs(userId);
    }

    @Test
    @DisplayName("Given a null user ID, when findAllDocs is called, then it should return whatever the repository returns for null")
    void givenNullUserId_whenFindAllDocs_thenReturnRepositoryResult() {
        // Arrange
        Long userId = null;
        List<Document> expectedDocs = new ArrayList<>();
        when(docRepository.findUserDocs(userId)).thenReturn(expectedDocs);

        // Act
        List<Document> actualDocs = docServiceImpl.findAllDocs(userId);

        // Assert
        assertNotNull(actualDocs);
        assertTrue(actualDocs.isEmpty());
        verify(docRepository, times(1)).findUserDocs(userId);
    }

    @Test
    @DisplayName("Given a negative user ID, when findAllDocs is called, then it should return the list of documents from the repository")
    void givenNegativeUserId_whenFindAllDocs_thenReturnListOfDocuments() {
        // Arrange
        Long userId = -1L;
        Document doc1 = new Document();
        List<Document> expectedDocs = Collections.singletonList(doc1);
        when(docRepository.findUserDocs(userId)).thenReturn(expectedDocs);

        // Act
        List<Document> actualDocs = docServiceImpl.findAllDocs(userId);

        // Assert
        assertNotNull(actualDocs);
        assertEquals(1,