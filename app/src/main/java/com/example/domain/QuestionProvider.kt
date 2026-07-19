package com.example.domain

import com.example.R

object QuestionProvider {
    val questions = listOf(
        Question(
            id = "latency_distributions",
            homeCardQuestion = "Two headline latency metrics improved. Why can users still be slower?",
            fullQuestion = "After a release, mean latency improves from 125 ms to 110 ms and p50 improves from 120 ms to 90 ms, but complaints about slowness increase. Which explanation is better?",
            imageRes = R.drawable.image_latency_1784495704928,
            compareOptions = listOf(
                Option("1", "The release may have accelerated the common path while making a smaller path much slower. Mean and p50 can improve even as p90 or p99 regress, especially when different request paths create multiple modes. Inspect tail percentiles, a lightly trimmed mean, the histogram or ECDF, and results segmented by request type and cohort."),
                Option("2", "Because both mean and p50 improved, latency improved for typical users and for the service overall. The complaints are probably anecdotal. P99 is too sensitive to unusual requests to guide the investigation, and a histogram adds little once the average and median agree.")
            ),
            correctCompareOptionId = "1",
            compareExplanation = "Mean and p50 describe important parts of the distribution, but neither rules out a severe regression affecting a smaller population. A slow cohort or request path can produce a second peak or a much heavier tail while both headline measurements improve.",
            diagnosisOptions = listOf(
                Option("1", "It treats mean and p50 as sufficient while dismissing tail behavior, and it ignores that different paths or cohorts can create a multimodal latency distribution."),
                Option("2", "It compares two different latency statistics when only maximum latency is valid, and it fails to discard every request above p99 before evaluating the release."),
                Option("3", "It assumes complaints can be related to latency, and it recommends segmentation even though valid latency analysis requires every request to have the same payload size.")
            ),
            correctDiagnosisOptionId = "1",
            diagnosisExplanation = "The incorrect answer makes two mistakes: it interprets improvement in the center as improvement for everyone, and it prevents investigation of precisely the tail and subpopulation behavior that could explain the complaints.",
            replacementOptions = listOf(
                Option("1", "Replace it with: 'The common path may have become faster while a smaller path regressed badly. Compare p90 and p99, inspect the histogram or ECDF for additional modes, compute an appropriately trimmed mean, and segment the results by request type, payload and user cohort.'"),
                Option("2", "Replace it with: 'The release probably introduced a few measurement errors. Remove the slowest one percent, recompute the mean, and use that as the primary decision metric because segmentation would make the result less statistically stable.'"),
                Option("3", "Replace it with: 'The release likely regressed the tail. Compare p99 before and after and roll back if it increased; a single tail percentile makes histograms and cohort-level analysis unnecessary.'")
            ),
            correctReplacementOptionId = "1",
            completeCorrectedAnswer = "The common path may have become faster while a smaller path regressed badly. Compare p90 and p99, inspect the histogram or ECDF for additional modes, compute an appropriately trimmed mean, and segment the results by request type, payload and user cohort.",
            takeaway = "Averages can improve while an important cohort becomes dramatically slower.",
            sourceUrls = listOf("https://research.google/pubs/the-tail-at-scale/")
        ),
        Question(
            id = "hbm_manufacturing",
            homeCardQuestion = "Is HBM manufactured on the same wafer as the accelerator?",
            fullQuestion = "A teammate says HBM3 is expensive because it is fabricated beside the accelerator on the same leading-edge wafer. Which answer is more accurate?",
            imageRes = R.drawable.image_hbm_1784495715145,
            compareOptions = listOf(
                Option("1", "The accelerator logic and HBM memory dies are normally fabricated on separate wafers and later integrated into one advanced package. HBM costs more per delivered bit because its wide interface and TSV layout reduce density, while thinning, stacking, testing, interposer assembly and compounded yield losses add cost."),
                Option("2", "The accelerator and HBM are normally fabricated together on one leading-edge logic wafer and separated only during packaging. HBM costs more mainly because its 1,024-bit interface consumes accelerator die area; stacking and interposer assembly add relatively little once the shared wafer passes testing.")
            ),
            correctCompareOptionId = "1",
            compareExplanation = "HBM and accelerator logic are physically close in the finished package, but that does not mean they originated on the same wafer. HBM is a stack of separately manufactured DRAM dies connected to the accelerator through advanced packaging.",
            diagnosisOptions = listOf(
                Option("1", "It confuses being in the same package with being fabricated on the same wafer, and it omits the substantial cost and yield effects of TSVs, die thinning, stacking and interposer assembly."),
                Option("2", "It incorrectly gives HBM a wide interface and assumes separate fabrication, even though HBM obtains its bandwidth primarily from very high clock rates and conventional DIMM packaging."),
                Option("3", "It discusses manufacturing structure even though HBM’s price is determined exclusively by temporary market demand and is unrelated to density, yield or package complexity.")
            ),
            correctDiagnosisOptionId = "1",
            diagnosisExplanation = "The GPU is fabricated as logic and the HBM dies as DRAM. They meet during packaging. The HBM stack and the large interposer introduce additional processing, testing, thermal and yield challenges.",
            replacementOptions = listOf(
                Option("1", "Replace it with: 'The accelerator and HBM dies generally come from separate logic and DRAM wafers and are integrated in the same package. HBM’s lower bit density, TSVs, thinning, multi-die stacking, testing, package complexity and compounded yield risk all increase cost.'"),
                Option("2", "Replace it with: 'HBM is fabricated on the accelerator wafer, but its cells use six-transistor SRAM rather than DRAM capacitors. The larger memory cells are the primary reason the completed package costs more.'"),
                Option("3", "Replace it with: 'HBM and the accelerator come from separate wafers, so manufacturing complexity is comparable to ordinary DDR. Nearly all of the price premium comes from temporary shortages of accelerator packages.'")
            ),
            correctReplacementOptionId = "1",
            completeCorrectedAnswer = "The accelerator and HBM dies generally come from separate logic and DRAM wafers and are integrated in the same package. HBM’s lower bit density, TSVs, thinning, multi-die stacking, testing, package complexity and compounded yield risk all increase cost.",
            takeaway = "HBM and the accelerator share a package, not a fabrication wafer.",
            sourceUrls = listOf(
                "https://docs.amd.com/r/en-US/am011-versal-acap-trm/High-Bandwidth-Memory-Interface",
                "https://3dfabric.tsmc.com/english/dedicatedFoundry/technology/cowos.htm"
            )
        ),
        Question(
            id = "on_policy_distillation",
            homeCardQuestion = "How can a smaller teacher improve a larger student?",
            fullQuestion = "A smaller task specialist produces sharper behavior than a larger general model. Which description of on-policy reverse-KL distillation is correct?",
            imageRes = R.drawable.image_distillation_1784495725407,
            compareOptions = listOf(
                Option("1", "Sample trajectories from the student, evaluate the relevant token distributions under the teacher, and minimize D_KL(p_student || p_teacher) on student-generated contexts. This can pull a larger-capacity student toward a smaller specialist’s sharper task behavior, but pure distillation cannot supply knowledge absent from the teacher signal."),
                Option("2", "Sample trajectories from the teacher, train the student using D_KL(p_teacher || p_student), and call the procedure on-policy reverse-KL. Because the student has greater capacity, the objective can discover task knowledge beyond the teacher while guaranteeing that every teacher mode remains covered.")
            ),
            correctCompareOptionId = "1",
            compareExplanation = "On-policy distillation uses contexts produced by the student. Reverse KL places the student distribution in the first argument. A smaller teacher can still provide a useful sharper distribution in a particular domain, but model capacity alone does not create supervision absent from the teacher signal.",
            diagnosisOptions = listOf(
                Option("1", "It reverses the on-policy sampling direction and KL orientation, and it claims that pure distillation can create knowledge not supplied by the teacher or other training signal."),
                Option("2", "It permits the teacher to be smaller than the student and uses token distributions, even though valid distillation requires a larger teacher and matching hidden-state dimensions."),
                Option("3", "It uses KL divergence rather than human A/B preferences, and it fails to introduce a separate reward model before the student can sample any trajectories.")
            ),
            correctDiagnosisOptionId = "1",
            diagnosisExplanation = "Teacher-generated samples with D_KL(p_teacher || p_student) describe the conventional forward-KL direction. On-policy reverse KL instead follows student-generated contexts and does not, by itself, provide information outside the teacher distribution.",
            replacementOptions = listOf(
                Option("1", "Replace it with: 'Generate trajectories from the student and compare its token distributions with the teacher using D_KL(p_student || p_teacher). This can transfer a smaller specialist’s sharper task behavior into a larger student, although additional data or objectives are required to exceed the teacher’s information.'"),
                Option("2", "Replace it with: 'Generate trajectories from the teacher and minimize D_KL(p_student || p_teacher). The student remains on-policy because it eventually reproduces the samples, and its additional parameters guarantee behavior beyond the teacher.'"),
                Option("3", "Replace it with: 'Generate trajectories from the student but minimize D_KL(p_teacher || p_student). Forward KL becomes mode-seeking on student contexts and therefore removes the need for any teacher knowledge beyond its highest-probability output.'")
            ),
            correctReplacementOptionId = "1",
            completeCorrectedAnswer = "Generate trajectories from the student and compare its token distributions with the teacher using D_KL(p_student || p_teacher). This can transfer a smaller specialist’s sharper task behavior into a larger student, although additional data or objectives are required to exceed the teacher’s information.",
            takeaway = "A smaller but sharper specialist can teach a larger student, but distillation does not manufacture missing supervision.",
            sourceUrls = listOf("https://thinkingmachines.ai/blog/on-policy-distillation/")
        ),
        Question(
            id = "math_estimation",
            homeCardQuestion = "Without expanding it, how many decimal digits are in 2^30?",
            fullQuestion = "Without calculating the exact value, how many decimal digits are in 2^30, and what is the best mental heuristic for this estimation?",
            imageRes = R.drawable.image_math_estimation_1784497519961,
            compareOptions = listOf(
                Option("1", "Since 2^10 is 1024, which is approximately 10^3, we can approximate 2^30 as (2^10)^3 ≈ (10^3)^3 = 10^9. A number in the low billions has 10 decimal digits."),
                Option("2", "Since 2^10 is 1024, which is roughly 10^3, we can approximate 2^30 as 10^3 * 30 = 30,000. Therefore, it has 5 decimal digits.")
            ),
            correctCompareOptionId = "1",
            compareExplanation = "The heuristic 2^10 ≈ 10^3 is correct, but the exponentiation rule is (x^a)^b = x^{ab}, meaning you raise the approximation to the power of 3, not multiply by 30.",
            diagnosisOptions = listOf(
                Option("1", "It incorrectly multiplies the approximation by the exponent rather than cubing it, resulting in a severe underestimation of the magnitude."),
                Option("2", "It assumes that 2^10 is exactly 10^3, failing to account for the accumulated error over 30 powers of two."),
                Option("3", "It correctly identifies the value but miscounts the number of decimal digits by omitting the leading zero.")
            ),
            correctDiagnosisOptionId = "1",
            diagnosisExplanation = "The incorrect answer misapplies the exponent rule, turning an exponential relationship into a multiplicative one, yielding 30,000 instead of ~1,000,000,000.",
            replacementOptions = listOf(
                Option("1", "Replace it with: 'Since 2^10 is 1024, which is approximately 10^3, we can approximate 2^30 as (2^10)^3 ≈ (10^3)^3 = 10^9. This means it is in the billions and has 10 decimal digits.'"),
                Option("2", "Replace it with: 'Since 2^30 is equivalent to 30 squared, the value is 900, meaning it has 3 decimal digits.'"),
                Option("3", "Replace it with: 'To find the digits, divide 30 by 2, which gives 15 digits.'")
            ),
            correctReplacementOptionId = "1",
            completeCorrectedAnswer = "Since 2^10 is 1024, which is approximately 10^3, we can approximate 2^30 as (2^10)^3 ≈ (10^3)^3 = 10^9. This means it is in the billions and has 10 decimal digits.",
            takeaway = "Use the heuristic 2^10 ≈ 10^3 to quickly estimate large powers of two in decimal.",
            sourceUrls = emptyList()
        ),
        Question(
            id = "scatter_gather",
            homeCardQuestion = "Why can a service fanning out to 100 healthy backends have poor tail latency?",
            fullQuestion = "A frontend service fans out a single user request to 100 healthy backend microservices and waits for all of them to reply. Why might the end-to-end p99 latency be remarkably poor?",
            imageRes = R.drawable.image_scatter_gather_1784497528528,
            compareOptions = listOf(
                Option("1", "The frontend must wait for the slowest of the 100 backends. Even if each backend has a great p99 latency, the probability that ALL 100 requests complete quickly is low, degrading the frontend's median latency."),
                Option("2", "Fanning out to 100 backends overwhelms the network switch with TCP syn floods. The latency degrades because the frontend must repeatedly retransmit the requests sequentially until all are acknowledged.")
            ),
            correctCompareOptionId = "1",
            compareExplanation = "The primary driver of poor tail latency in fan-out architectures is statistical: the overall response time is dictated by the maximum of the individual response times.",
            diagnosisOptions = listOf(
                Option("1", "It attributes the slowdown to network flooding and sequential retransmission, missing the fundamental statistical reality of waiting for the slowest of 100 independent parallel requests."),
                Option("2", "It incorrectly assumes backends are dropping packets, when in fact the latency is caused by the frontend running out of thread pool workers."),
                Option("3", "It blames TCP syn floods instead of UDP congestion, and fails to recognize backend health checks as the true source of delay.")
            ),
            correctDiagnosisOptionId = "1",
            diagnosisExplanation = "The incorrect answer focuses on unlikely catastrophic network failures instead of the inescapable math of parallel scatter-gather operations.",
            replacementOptions = listOf(
                Option("1", "Replace it with: 'The frontend's latency is determined by the slowest backend response. With 100 parallel requests, a 1% chance of a slow response from any backend translates to a ~63% chance of end-to-end delay.'"),
                Option("2", "Replace it with: 'The frontend's latency degrades because it takes time to parse 100 JSON responses. Moving to Protobuf will resolve the tail latency issue completely.'"),
                Option("3", "Replace it with: 'The tail latency is poor because the frontend must establish 100 TLS handshakes sequentially.'")
            ),
            correctReplacementOptionId = "1",
            completeCorrectedAnswer = "The frontend's latency is determined by the slowest backend response. With 100 parallel requests, a 1% chance of a slow response from any backend translates to a ~63% chance of end-to-end delay.",
            takeaway = "In a scatter-gather architecture, your median latency becomes the tail latency of your backends.",
            sourceUrls = listOf("https://research.google/pubs/the-tail-at-scale/")
        ),
        Question(
            id = "rag_failures",
            homeCardQuestion = "How can you distinguish RAG retrieval failures from generation failures?",
            fullQuestion = "A user submits a query to a Retrieval-Augmented Generation (RAG) pipeline and receives a factually incorrect answer. How can you isolate whether the failure occurred in retrieval or generation?",
            imageRes = R.drawable.image_rag_pipeline_1784497538500,
            compareOptions = listOf(
                Option("1", "Inspect the context chunks provided to the LLM. If the chunks do not contain the necessary facts, it is a retrieval failure. If the chunks contain the facts but the LLM hallucinates, it is a generation failure."),
                Option("2", "Check the embedding model's cosine similarity score. If the score is below 0.8, it is a retrieval failure. If the score is above 0.8 but the answer is wrong, you must lower the temperature to 0.0.")
            ),
            correctCompareOptionId = "1",
            compareExplanation = "Cosine similarity thresholds don't guarantee that the text actually contains the semantic answer. You must evaluate the retrieved context against the query independently.",
            diagnosisOptions = listOf(
                Option("1", "It relies on an arbitrary cosine similarity threshold to define retrieval success, and incorrectly assumes that a temperature of 0.0 eliminates generation failures."),
                Option("2", "It suggests lowering the temperature to 0.0, which actually increases hallucinations, and it fails to account for chunk overlap."),
                Option("3", "It uses cosine similarity instead of dot product, and ignores that generation failures require fine-tuning to identify.")
            ),
            correctDiagnosisOptionId = "1",
            diagnosisExplanation = "The incorrect answer trusts a similarity metric rather than the actual retrieved text to determine if facts were present, and assumes low temperature guarantees accuracy.",
            replacementOptions = listOf(
                Option("1", "Replace it with: 'Read the retrieved context. If the facts needed to answer the query are absent, the retriever failed. If the facts are present but the LLM produced a wrong answer, the generator failed.'"),
                Option("2", "Replace it with: 'Switch from a vector database to a graph database. If the answer improves, it was a retrieval failure.'"),
                Option("3", "Replace it with: 'Evaluate token generation speed. A retrieval failure results in high latency before the first token.'")
            ),
            correctReplacementOptionId = "1",
            completeCorrectedAnswer = "Read the retrieved context. If the facts needed to answer the query are absent, the retriever failed. If the facts are present but the LLM produced a wrong answer, the generator failed.",
            takeaway = "Isolate RAG failures by inspecting the retrieved context as a standalone document.",
            sourceUrls = emptyList()
        )
    )
}
