package pl.dk.aibron_first_task.event_archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EventArchiveRepository extends JpaRepository<EventArchive, Long> {

}
