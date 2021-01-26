package fourkeymetrics.metrics.controller

import fourkeymetrics.metrics.calculator.DeploymentFrequencyService
import fourkeymetrics.dashboard.service.PipelineService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(controllers = [DeploymentFrequencyController::class])
internal class DeploymentFrequencyControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var deploymentFrequencyService: DeploymentFrequencyService

    @MockBean
    private lateinit var pipelineService: PipelineService

    @Test
    internal fun `should get deployment count given time range and pipeline information`() {
        val dashboardId = "dashboard ID"
        val pipelineId = "test pipeline ID"
        val targetStage = "UAT"
        val startTimestamp = 1609459200000L
        val endTimestamp = 1611964800000L

        `when`(pipelineService.hasPipeline(dashboardId, pipelineId)).thenReturn(true)
        `when`(deploymentFrequencyService.getDeploymentCount(pipelineId, targetStage, startTimestamp, endTimestamp))
            .thenReturn(30)
        `when`(pipelineService.hasStageInTimeRange(pipelineId, targetStage, startTimestamp, endTimestamp)).thenReturn(true)


        mockMvc.perform(get("/api/deployment-frequency")
            .param("dashboardId", dashboardId)
            .param("pipelineId", pipelineId)
            .param("targetStage", targetStage)
            .param("startTimestamp", startTimestamp.toString())
            .param("endTimestamp", endTimestamp.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.deploymentCount").value(30))
    }

    @Test
    internal fun `should return bad request given start time is after end time when get deployment count`() {
        val dashboardId = "dashboard ID"
        val pipelineId = "test pipeline ID"
        val targetStage = "UAT"
        val startTimestamp = 1611964800000L
        val endTimestamp = 1609459200000L

        `when`(pipelineService.hasPipeline(dashboardId, pipelineId)).thenReturn(true)
        `when`(pipelineService.hasStageInTimeRange(pipelineId, targetStage, startTimestamp, endTimestamp)).thenReturn(true)

        mockMvc.perform(get("/api/deployment-frequency")
            .param("dashboardId", dashboardId)
            .param("pipelineId", pipelineId)
            .param("targetStage", targetStage)
            .param("startTimestamp", startTimestamp.toString())
            .param("endTimestamp", endTimestamp.toString()))
            .andExpect(status().isBadRequest)
    }

    @Test
    internal fun `should return bad request given pipeline ID does not exist when get deployment count`() {
        val dashboardId = "dashboard ID"
        val pipelineId = "invalid pipeline ID"
        val targetStage = "UAT"
        val startTimestamp = 1609459200000L
        val endTimestamp = 1611964800000L

        `when`(pipelineService.hasPipeline(dashboardId, pipelineId)).thenReturn(false)
        `when`(pipelineService.hasStageInTimeRange(pipelineId, targetStage, startTimestamp, endTimestamp)).thenReturn(true)

        mockMvc.perform(get("/api/deployment-frequency")
            .param("dashboardId", dashboardId)
            .param("pipelineId", pipelineId)
            .param("targetStage", targetStage)
            .param("startTimestamp", startTimestamp.toString())
            .param("endTimestamp", endTimestamp.toString()))
            .andExpect(status().isBadRequest)
    }

    @Test
    internal fun `should return bad request given Stage name does not exist when get deployment count`() {
        val dashboardId = "dashboard ID"
        val pipelineId = "pipeline ID"
        val targetStage = "invalid stage"
        val startTimestamp = 1609459200000L
        val endTimestamp = 1611964800000L

        `when`(pipelineService.hasPipeline(dashboardId, pipelineId)).thenReturn(true)
        `when`(pipelineService.hasStageInTimeRange(pipelineId, targetStage, startTimestamp, endTimestamp)).thenReturn(false)

        mockMvc.perform(get("/api/deployment-frequency")
            .param("dashboardId", dashboardId)
            .param("pipelineId", pipelineId)
            .param("targetStage", targetStage)
            .param("startTimestamp", startTimestamp.toString())
            .param("endTimestamp", endTimestamp.toString()))
            .andExpect(status().isBadRequest)
    }
}